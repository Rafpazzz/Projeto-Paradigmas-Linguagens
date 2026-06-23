# Prompt para Codex - Sistema de Recomendação de Filmes em Java OO

Você deve implementar um sistema simples de recomendação de filmes em Java, usando Programação Orientada a Objetos.

O objetivo é demonstrar claramente:

- Encapsulamento
- Classes com responsabilidades bem definidas
- Clean Code
- Princípios SOLID
- Strategy Pattern
- Open/Closed Principle

Não crie um código complexo. O foco é ser simples, legível, didático e fiel ao paradigma OO.

## Contexto do projeto

O projeto é um sistema de recomendação de filmes baseado no perfil de um usuário.

O fluxo principal esperado é:

1. Criar um objeto `PerfilUsuario`.
2. Passar esse perfil para o sistema de recomendação.
3. Gerar a lista de filmes recomendados com base nesse perfil.
4. Atualizar o mesmo objeto `PerfilUsuario` com novos dados.
5. Executar novamente a recomendação usando o mesmo objeto atualizado.

Exemplo conceitual do fluxo na `Main`:

```java
PerfilUsuario perfil = new PerfilUsuario(...);

List<FilmeRecomendado> primeiraRecomendacao = motorRecomendacao.recomendar(catalogo, perfil);

perfil.atualizarPerfil(...);

List<FilmeRecomendado> segundaRecomendacao = motorRecomendacao.recomendar(catalogo, perfil);
```

O sistema não deve gerar uma saída fixa.
A recomendação precisa ser calculada dinamicamente a partir do perfil recebido.

## Entrada do usuário

O perfil do usuário possui:

- `generosFavoritos`: lista de strings
- `humor`: string com um dos valores: `animado`, `reflexivo` ou `triste`
- `duracaoMaxima`: inteiro em minutos
- `classificacaoEtaria`: `livre`, `12`, `14` ou `18`

Use strings normalizadas sem acento no código:

- `acao`
- `comedia`
- `ficcao_cientifica`
- `drama`
- `romance`

## Saída esperada

O sistema deve retornar uma lista de filmes recomendados, ordenada do mais relevante para o menos relevante.

Para cada filme recomendado, mostrar:

- título
- gênero(s)
- duração
- classificação etária
- pontuação de relevância

## Regras obrigatórias de recomendação

Implemente estas regras:

### R1 - Gênero

O filme deve possuir pelo menos um gênero presente na lista de gêneros favoritos do usuário.

### R2 - Duração

A duração do filme deve ser menor ou igual à duração máxima aceita pelo usuário.

### R3 - Classificação

A classificação etária do filme deve ser compatível com a classificação permitida pelo usuário.

A ordem é:

```text
livre <= 12 <= 14 <= 18
```

Exemplo:

- Usuário com classificação `14` pode assistir filmes `livre`, `12` e `14`.
- Usuário com classificação `14` não pode assistir filmes `18`.

### R4 - Humor

O humor prioriza alguns gêneros:

- `animado`: `acao` e `comedia`
- `reflexivo`: `drama` e `ficcao_cientifica`
- `triste`: `comedia` e `romance`

### R5 - Pontuação mínima

Somente recomendar filmes com pontuação maior ou igual a 2.

## Cálculo da pontuação

Crie obrigatoriamente uma classe chamada:

```java
CalculadoraPontuacao
```

Essa classe deve ser instanciada e chamada na classe `Main`.

Ela deve ter um método público responsável por calcular a pontuação de um filme com base no perfil do usuário.

Exemplo:

```java
public int calcular(Filme filme, PerfilUsuario perfilUsuario)
```

A pontuação deve ser calculada assim:

- `+1` ponto para cada gênero do filme que esteja em `generosFavoritos` do usuário.
- `+1` ponto se algum gênero do filme coincidir com os gêneros priorizados pelo humor informado.

Exemplo:

Usuário:

```text
generosFavoritos: ["acao", "drama"]
humor: "animado"
```

Filme:

```text
Tropa de Elite
generos: ["acao", "drama"]
```

Pontuação:

```text
+1 por acao estar nos favoritos
+1 por drama estar nos favoritos
+1 porque humor animado prioriza acao
Total: 3 pontos
```

Importante:

- Duração e classificação são critérios de filtro.
- Duração e classificação não devem somar pontos.
- A pontuação deve ser calculada sempre com base no perfil recebido.
- Não crie pontuação fixa para nenhum filme.
- Não faça hardcode da saída do caso de teste.

## Base de filmes obrigatória

Use exatamente esta base de filmes:

1. Duna
   - gêneros: `ficcao_cientifica`
   - duração: `155`
   - classificação: `12`

2. Vingadores
   - gêneros: `acao`
   - duração: `149`
   - classificação: `12`

3. Forrest Gump
   - gêneros: `drama`
   - duração: `142`
   - classificação: `livre`

4. Superbad
   - gêneros: `comedia`
   - duração: `113`
   - classificação: `18`

5. La La Land
   - gêneros: `romance`
   - duração: `128`
   - classificação: `livre`

6. Mad Max
   - gêneros: `acao`
   - duração: `120`
   - classificação: `14`

7. Her
   - gêneros: `ficcao_cientifica`, `drama`
   - duração: `126`
   - classificação: `14`

8. Comer Rezar Amar
   - gêneros: `romance`
   - duração: `133`
   - classificação: `livre`

9. Tropa de Elite
   - gêneros: `acao`, `drama`
   - duração: `115`
   - classificação: `18`

10. Interestelar
    - gêneros: `ficcao_cientifica`
    - duração: `169`
    - classificação: `livre`

11. Clueless
    - gêneros: `comedia`, `romance`
    - duração: `97`
    - classificação: `14`

12. O Poderoso Chefão
    - gêneros: `drama`
    - duração: `175`
    - classificação: `14`

## Estrutura obrigatória do projeto

Crie a seguinte estrutura:

```text
orientado-objetos/
  README.md
  src/
    Main.java
    Filme.java
    PerfilUsuario.java
    ClassificacaoEtaria.java
    CriterioRecomendacao.java
    CriterioGenero.java
    CriterioDuracao.java
    CriterioClassificacao.java
    CalculadoraPontuacao.java
    FilmeRecomendado.java
    MotorRecomendacao.java
    CatalogoFilmes.java
```

Não use Maven, Gradle, Spring, Lombok ou qualquer dependência externa.
Use apenas Java puro e bibliotecas padrão.

## Responsabilidades das classes

### Filme

Representa um filme.

Atributos privados:

- `titulo`
- `generos`
- `duracaoMinutos`
- `classificacaoEtaria`

Use encapsulamento.
Não deixe atributos públicos.

### PerfilUsuario

Representa o perfil informado pelo usuário.

Atributos privados:

- `generosFavoritos`
- `humor`
- `duracaoMaxima`
- `classificacaoEtaria`

Essa classe deve ter um método para atualizar o perfil usando o mesmo objeto.

Exemplo:

```java
public void atualizarPerfil(
    List<String> novosGenerosFavoritos,
    String novoHumor,
    int novaDuracaoMaxima,
    ClassificacaoEtaria novaClassificacaoEtaria
)
```

Esse método será usado na `Main` para demonstrar o fluxo:

1. Criar perfil.
2. Recomendar filmes.
3. Atualizar o mesmo perfil.
4. Recomendar novamente.

### ClassificacaoEtaria

Crie um enum para representar:

- `LIVRE`
- `DOZE`
- `CATORZE`
- `DEZOITO`

Cada valor deve possuir um nível numérico:

- `LIVRE = 0`
- `DOZE = 12`
- `CATORZE = 14`
- `DEZOITO = 18`

Crie um método para verificar compatibilidade.

Exemplo:

```java
public boolean permiteAssistir(ClassificacaoEtaria classificacaoDoFilme)
```

### CriterioRecomendacao

Interface do Strategy Pattern.

Deve conter:

```java
boolean aceita(Filme filme, PerfilUsuario perfilUsuario);
```

### CriterioGenero

Implementa `CriterioRecomendacao`.

Verifica se o filme possui pelo menos um gênero favorito do usuário.

### CriterioDuracao

Implementa `CriterioRecomendacao`.

Verifica se a duração do filme é menor ou igual à duração máxima do usuário.

### CriterioClassificacao

Implementa `CriterioRecomendacao`.

Verifica se a classificação etária do filme é compatível com a classificação permitida ao usuário.

### CalculadoraPontuacao

Classe responsável apenas pelo cálculo da pontuação.

Ela deve ser instanciada na `Main` e passada para o `MotorRecomendacao`.

Exemplo:

```java
CalculadoraPontuacao calculadoraPontuacao = new CalculadoraPontuacao();
MotorRecomendacao motorRecomendacao = new MotorRecomendacao(criterios, calculadoraPontuacao);
```

Ela deve conter:

```java
public int calcular(Filme filme, PerfilUsuario perfilUsuario)
```

Também deve conter uma lógica interna para mapear humor para gêneros priorizados:

- `animado` -> `acao`, `comedia`
- `reflexivo` -> `drama`, `ficcao_cientifica`
- `triste` -> `comedia`, `romance`

Essa classe não deve filtrar filmes.
Ela apenas calcula pontuação.

### FilmeRecomendado

Representa o resultado final da recomendação.

Deve conter:

- `Filme`
- `pontuacao`

### MotorRecomendacao

Recebe no construtor:

- lista de critérios de recomendação
- calculadora de pontuação

Deve possuir o método:

```java
public List<FilmeRecomendado> recomendar(
    List<Filme> filmes,
    PerfilUsuario perfilUsuario
)
```

Esse método deve:

1. Percorrer os filmes.
2. Aplicar todos os critérios.
3. Calcular a pontuação usando `CalculadoraPontuacao`.
4. Manter apenas filmes com pontuação maior ou igual a 2.
5. Ordenar por pontuação decrescente.
6. Em caso de empate, ordenar por título em ordem alfabética.
7. Retornar a lista de `FilmeRecomendado`.

Importante:

O `MotorRecomendacao` não deve saber como a pontuação é calculada internamente.
Ele apenas chama a `CalculadoraPontuacao`.

O `MotorRecomendacao` também não deve conhecer os detalhes internos dos critérios.
Ele deve depender da interface `CriterioRecomendacao`.

Isso demonstra:

- Dependency Inversion Principle
- Open/Closed Principle
- Strategy Pattern

### CatalogoFilmes

Classe responsável por criar e retornar a lista fixa de filmes.

### Main

A classe `Main` deve apenas demonstrar o uso do sistema.

Ela deve:

1. Criar o catálogo de filmes.
2. Criar o primeiro perfil de usuário.
3. Criar a lista de critérios.
4. Instanciar a `CalculadoraPontuacao`.
5. Instanciar o `MotorRecomendacao`.
6. Executar a primeira recomendação.
7. Imprimir o resultado.
8. Atualizar o mesmo objeto `PerfilUsuario`.
9. Executar a segunda recomendação.
10. Imprimir o novo resultado.

Não coloque regras de negócio na `Main`.

Exemplo de fluxo obrigatório na `Main`:

```java
PerfilUsuario perfilUsuario = new PerfilUsuario(
    List.of("acao", "ficcao_cientifica"),
    "animado",
    150,
    ClassificacaoEtaria.CATORZE
);

imprimirRecomendacoes(
    "Primeira recomendação",
    motorRecomendacao.recomendar(catalogo, perfilUsuario)
);

perfilUsuario.atualizarPerfil(
    List.of("drama", "romance"),
    "reflexivo",
    180,
    ClassificacaoEtaria.DEZOITO
);

imprimirRecomendacoes(
    "Segunda recomendação",
    motorRecomendacao.recomendar(catalogo, perfilUsuario)
);
```

## Requisitos de Clean Code

Siga estas regras:

- Use nomes claros.
- Evite métodos grandes.
- Evite duplicação de código.
- Cada classe deve ter uma responsabilidade principal.
- Use atributos privados.
- Use getters somente quando necessário.
- Não espalhe regras de recomendação dentro da `Main`.
- Não use herança desnecessária.
- Prefira composição e interfaces.
- Não use código estático para a lógica principal de recomendação.

## Requisitos SOLID

O código deve demonstrar:

### Single Responsibility Principle

Cada classe deve ter uma responsabilidade clara.

Exemplo:

- `Filme` representa um filme.
- `PerfilUsuario` representa o perfil.
- `CalculadoraPontuacao` calcula pontuação.
- `MotorRecomendacao` coordena o processo de recomendação.
- Cada critério valida apenas uma regra.

### Open/Closed Principle

Para adicionar um novo critério, como `nota IMDb maior que 7`, deve ser possível criar uma nova classe que implemente `CriterioRecomendacao`, sem alterar o `MotorRecomendacao`.

### Dependency Inversion Principle

`MotorRecomendacao` deve depender da interface `CriterioRecomendacao`, não das implementações concretas.

## README.md obrigatório

Crie um arquivo `README.md` dentro da pasta `orientado-objetos`.

O README deve documentar apenas o paradigma Orientado a Objetos.

O README deve conter:

```markdown
# Paradigma Orientado a Objetos - Java

## Descrição

Explique que a solução usa classes para representar entidades do domínio e Strategy Pattern para encapsular critérios de recomendação.

Explique também que a pontuação é calculada por uma classe própria chamada CalculadoraPontuacao.

## Versão utilizada

Java 17  
javac 17

## Como compilar

\```bash
javac -d out src/*.java
\```

## Como executar

\```bash
java -cp out Main
\```

## Dependências externas

Não há dependências externas.  
O projeto usa apenas bibliotecas padrão do Java.

## Estrutura da pasta

Mostre a estrutura dos arquivos.

## Fluxo do programa

Explique:

1. Um perfil de usuário é criado.
2. O sistema gera recomendações.
3. O mesmo objeto de perfil é atualizado.
4. O sistema gera novas recomendações com base no perfil atualizado.

## Paradigma OO aplicado

Explique brevemente:

- Encapsulamento
- Strategy Pattern
- Separação de responsabilidades
- Open/Closed Principle
- Dependency Inversion Principle
```

## Resultado esperado da resposta do Codex

Gere todos os arquivos Java completos e o `README.md`.

Não escreva apenas explicações.
Crie o código completo.

O código deve compilar e executar usando exatamente os comandos do README.

Mantenha o código simples, didático e orientado a objetos.
