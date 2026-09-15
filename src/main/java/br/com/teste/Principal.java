package br.com.teste;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Requisito 3 - Classe Principal que executa todas as acoes do teste.
 */
public class Principal {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat FORMATO_NUMERO = criarFormatoNumerico();
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");
    private static final BigDecimal PERCENTUAL_AUMENTO = new BigDecimal("0.10");

    public static void main(String[] args) {

        List<Funcionario> funcionarios = criarFuncionarios();

        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        titulo("3.3 - Lista de funcionarios");
        funcionarios.forEach(funcionario -> System.out.println(formatar(funcionario)));

        aplicarAumento(funcionarios, PERCENTUAL_AUMENTO);
        titulo("3.4 - Funcionarios apos 10% de aumento");
        funcionarios.forEach(funcionario -> System.out.println(formatar(funcionario)));

        Map<String, List<Funcionario>> funcionariosPorFuncao = agruparPorFuncao(funcionarios);

        titulo("3.6 - Funcionarios agrupados por funcao");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println(funcao);
            lista.forEach(funcionario -> System.out.println("   " + formatar(funcionario)));
        });

        // 3.8 - Imprimir os funcionarios que fazem aniversario nos meses 10 e 12.
        // (o enunciado nao possui o item 3.7)
        titulo("3.8 - Aniversariantes dos meses 10 e 12");
        funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(funcionario -> System.out.println(formatar(funcionario)));

        // 3.9 - Imprimir o funcionario com a maior idade (nome e idade).
        titulo("3.9 - Funcionario com a maior idade");
        Optional<Funcionario> maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento));

        maisVelho.ifPresent(funcionario -> System.out.printf(
                "Nome: %s | Idade: %d anos%n",
                funcionario.getNome(),
                calcularIdade(funcionario.getDataNascimento())));

        titulo("3.10 - Funcionarios em ordem alfabetica");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(funcionario -> System.out.println(formatar(funcionario)));

        titulo("3.11 - Total dos salarios");
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total: R$ " + FORMATO_NUMERO.format(totalSalarios));

        titulo("3.12 - Salarios minimos por funcionario (minimo = R$ "
                + FORMATO_NUMERO.format(SALARIO_MINIMO) + ")");
        funcionarios.forEach(funcionario -> {
            BigDecimal quantidade = funcionario.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.printf("%-10s %s salarios minimos%n",
                    funcionario.getNome(),
                    FORMATO_NUMERO.format(quantidade));
        });
    }

    /**
     * 3.1 - Cria a lista de funcionarios na mesma ordem da tabela do enunciado.
     * Usa ArrayList porque a lista precisa ser modificavel (remocao e aumento).
     */
    private static List<Funcionario> criarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
        return funcionarios;
    }

    /**
     * 3.4 - Aplica o percentual de aumento sobre o salario atual de cada funcionario.
     */
    private static void aplicarAumento(List<Funcionario> funcionarios, BigDecimal percentual) {
        funcionarios.forEach(funcionario -> {
            BigDecimal novoSalario = funcionario.getSalario()
                    .multiply(BigDecimal.ONE.add(percentual))
                    .setScale(2, RoundingMode.HALF_UP);
            funcionario.setSalario(novoSalario);
        });
    }

    /**
     * 3.5 - Agrupa os funcionarios por funcao.
     */
    private static Map<String, List<Funcionario>> agruparPorFuncao(List<Funcionario> funcionarios) {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    private static int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    /**
     * Data no formato dd/MM/yyyy e valores com ponto de milhar e virgula decimal.
     */
    private static String formatar(Funcionario funcionario) {
        return String.format("%-10s %s  R$ %12s  %s",
                funcionario.getNome(),
                funcionario.getDataNascimento().format(FORMATO_DATA),
                FORMATO_NUMERO.format(funcionario.getSalario()),
                funcionario.getFuncao());
    }

    /**
     * Define explicitamente os separadores para nao depender do Locale da maquina.
     */
    private static DecimalFormat criarFormatoNumerico() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        return new DecimalFormat("#,##0.00", simbolos);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}
