# Teste Prático – Programação (Java)

Projeto Java que implementa os requisitos do teste prático de cadastro de funcionários
de uma indústria.

## Stack

- Java 17+
- Sem dependências externas (apenas biblioteca padrão)
- Estrutura Maven padrão (`src/main/java`)

## Estrutura

```
src/main/java/br/com/teste/
├── Pessoa.java         # Requisito 1 – nome (String), dataNascimento (LocalDate)
├── Funcionario.java    # Requisito 2 – estende Pessoa + salario (BigDecimal), funcao (String)
└── Principal.java      # Requisito 3 – executa todas as ações (3.1 a 3.12)
```

## Como executar

Com Maven:

```bash
mvn compile exec:java -Dexec.mainClass=br.com.teste.Principal
```

Sem Maven (apenas JDK):

```bash
javac -encoding UTF-8 -d target/classes src/main/java/br/com/teste/*.java
java -Dstdout.encoding=UTF-8 -cp target/classes br.com.teste.Principal
```

> `-Dstdout.encoding=UTF-8` garante a exibição correta de acentos no console
> (necessário principalmente no Windows).

## Requisitos implementados

| Item | Descrição | Onde |
|------|-----------|------|
| 3.1  | Inserir todos os funcionários na ordem da tabela | `criarFuncionarios()` |
| 3.2  | Remover o funcionário "João" | `removeIf` no `main` |
| 3.3  | Imprimir todos os funcionários formatados | `formatar()` |
| 3.4  | Aplicar 10% de aumento e atualizar a lista | `aplicarAumento()` |
| 3.5  | Agrupar funcionários por função em um `Map` | `agruparPorFuncao()` |
| 3.6  | Imprimir os funcionários agrupados por função | `main` |
| 3.8  | Aniversariantes dos meses 10 e 12 | `main` |
| 3.9  | Funcionário com a maior idade (nome e idade) | `calcularIdade()` |
| 3.10 | Lista em ordem alfabética | `main` |
| 3.11 | Total dos salários | `main` |
| 3.12 | Quantos salários mínimos cada um ganha | `main` |

> O enunciado não possui o item **3.7** – a numeração salta de 3.6 para 3.8.

## Decisões técnicas

- **`BigDecimal` sempre construído a partir de `String`** (`new BigDecimal("2009.44")`).
  Construir a partir de `double` introduz erro de representação binária.
- **`RoundingMode.HALF_UP` explícito** em multiplicações e divisões, evitando
  `ArithmeticException` em dízimas e garantindo arredondamento previsível.
- **`DecimalFormatSymbols` configurado manualmente** (`.` para milhar, `,` para decimal)
  em vez de depender do `Locale` da máquina onde o programa roda.
- **`ArrayList`** em vez de `List.of(...)`, porque a lista precisa ser mutável
  (remoção no item 3.2 e atualização de salários no 3.4).
