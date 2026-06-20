# Prompt — Projeto Orientado a Objetos em Java

## Contexto

Quero que você implemente o paradigma **Orientado a Objetos** em Java para um sistema de recomendação de filmes por perfil de usuário.

O projeto deve seguir fielmente o enunciado do trabalho **“Sistema de Recomendação com Regras”**, no domínio de recomendação de filmes por perfil de usuário.

A linguagem deve ser **Java**.

---

## Objetivo

Criar uma implementação orientada a objetos que modele corretamente as entidades do problema e encapsule as regras de recomendação usando boas práticas de **Clean Code** e princípios **SOLID**, especialmente o **Open/Closed Principle**.

---

## Regras gerais do sistema

O sistema recebe um perfil de usuário com:

- `generos_favoritos`: lista de strings ou enums
- `humor`: `animado`, `reflexivo` ou `triste`
- `duracao_max`: inteiro em minutos
- `classificacao`: `livre`, `12`, `14` ou `18`

O sistema deve retornar uma lista de filmes recomendados, ordenada da maior pontuação para a menor.

Para cada filme recomendado, exibir:

- Título
- Gênero(s)
- Duração em minutos
- Classificação etária
- Pontuação de relevância

---

## Base de filmes obrigatória

Use exatamente esta base, sem alterar os dados originais:

| # | Título | Gênero(s) | Duração | Classificação |
|---|---|---|---:|---|
| 1 | Duna | ficção científica | 155 min | 12 |
| 2 | Vingadores | ação | 149 min | 12 |
| 3 | Forrest Gump | drama | 142 min | livre |
| 4 | Superbad | comédia | 113 min | 18 |
| 5 | La La Land | romance | 128 min | livre |
| 6 | Mad Max | ação | 120 min | 14 |
| 7 | Her | ficção científica, drama | 126 min | 14 |
| 8 | Comer Rezar Amar | romance | 133 min | livre |
| 9 | Tropa de Elite | ação, drama | 115 min | 18 |
| 10 | Interestelar | ficção científica | 169 min | livre |
| 11 | Clueless | comédia, romance | 97 min | 14 |
| 12 | O Poderoso Chefão | drama | 175 min | 14 |

---

## Normalização

No código, prefira representar os gêneros e classificações com `enum` para evitar problemas com acentos e strings soltas.

Exemplo:

- `ação` pode ser representado como `ACAO`
- `ficção científica` pode ser representado como `FICCAO_CIENTIFICA`
- `comédia` pode ser representado como `COMEDIA`
- `drama` pode ser representado como `DRAMA`
- `romance` pode ser representado como `ROMANCE`

---

## Humores

- `ANIMADO` prioriza `ACAO` e `COMEDIA`
- `REFLEXIVO` prioriza `DRAMA` e `FICCAO_CIENTIFICA`
- `TRISTE` prioriza `COMEDIA` e `ROMANCE`

---

## Lógica de recomendação

Implemente os critérios obrigatórios:

### R1 — Gênero

O filme deve ter pelo menos um gênero presente nos gêneros favoritos do usuário.

### R2 — Duração

A duração do filme deve ser menor ou igual à `duracao_max` do usuário.

### R3 — Classificação

A classificação do filme deve ser compatível com a classificação do usuário, considerando a ordem:

```text
LIVRE <= 12 <= 14 <= 18
```

Um usuário com classificação `14` pode assistir filmes `livre`, `12` e `14`, mas não `18`.

### R4 — Humor

O humor do usuário define gêneros priorizados:

- `animado`: ação e comédia
- `reflexivo`: drama e ficção científica
- `triste`: comédia e romance

### R5 — Mínimo

Somente recomendar filmes com pontuação maior ou igual a `2`.

---

## Lógica de pontuação

A pontuação deve respeitar o caso de teste obrigatório do enunciado.

Use esta lógica para que o resultado bata com a saída esperada:

- `+1` ponto por cada gênero do filme que esteja nos gêneros favoritos do usuário
- `+1` ponto se a duração do filme for menor ou igual à duração máxima do usuário
- `+1` ponto se algum gênero do filme coincidir com os gêneros priorizados pelo humor informado
- A classificação etária deve ser usada como filtro obrigatório, mas não deve somar ponto
- Filmes incompatíveis com a classificação do usuário não devem ser recomendados
- Filmes com pontuação menor que `2` não devem ser recomendados

---

## Caso de teste obrigatório

### Entrada

```text
generos_favoritos: [ACAO, FICCAO_CIENTIFICA]
humor: ANIMADO
duracao_max: 150
classificacao: 14
```

### Saída esperada

```text
[3 pts] Mad Max (ação, 120 min, 14)
[3 pts] Vingadores (ação, 149 min, 12)
[2 pts] Her (ficção científica + drama, 126 min, 14)
```

> Observação: Mad Max e Vingadores podem aparecer em qualquer ordem entre si, pois têm a mesma pontuação. Her deve aparecer por último.

---

## Estrutura obrigatória orientada a objetos

Crie pelo menos as seguintes classes/interfaces:

---

### 1. `Filme`

Responsabilidade: representar um filme.

Atributos:

- `titulo`
- `generos`
- `duracaoMinutos`
- `classificacao`

Boas práticas:

- atributos privados
- construtor
- getters
- não expor listas mutáveis diretamente

---

### 2. `PerfilUsuario`

Responsabilidade: representar as preferências do usuário.

Atributos:

- `generosFavoritos`
- `humor`
- `duracaoMaxima`
- `classificacao`

Boas práticas:

- atributos privados
- construtor
- getters
- validar entradas quando fizer sentido

---

### 3. `CriterioRecomendacao`

Interface para critérios de recomendação.

Métodos sugeridos:

```java
boolean aceita(Filme filme, PerfilUsuario perfil);
int calcularPontuacao(Filme filme, PerfilUsuario perfil);
```

A ideia é permitir que novos critérios sejam adicionados sem modificar as classes existentes.

---

### 4. Critérios concretos

Crie classes separadas para cada regra:

- `CriterioGenero`
- `CriterioDuracao`
- `CriterioClassificacao`
- `CriterioHumor`

Responsabilidades:

- `CriterioGenero` deve verificar se o filme tem gênero favorito e somar pontos por cada gênero favorito encontrado.
- `CriterioDuracao` deve verificar se a duração é compatível e somar `1` ponto se for.
- `CriterioClassificacao` deve apenas filtrar filmes incompatíveis, sem somar pontos.
- `CriterioHumor` deve somar `1` ponto se algum gênero do filme bater com os gêneros priorizados pelo humor.

---

### 5. `MotorRecomendacao`

Responsabilidade: receber uma lista de critérios e aplicar a recomendação.

Deve:

- receber os critérios no construtor
- calcular a pontuação total de cada filme
- filtrar filmes que não passam nos critérios obrigatórios
- filtrar filmes com pontuação menor que `2`
- ordenar os filmes por pontuação decrescente
- retornar uma lista de resultados recomendados

---

### 6. `Recomendacao`

Classe para representar o resultado da recomendação.

Atributos:

- `Filme filme`
- `int pontuacao`

---

### 7. `CatalogoFilmes` ou `FilmeRepositoryFake`

Responsabilidade: fornecer a base fixa de filmes do enunciado.

Como é um trabalho acadêmico, pode ser uma classe simples que retorna uma lista imutável de filmes.

---

### 8. `Main`

Classe apenas para executar o caso de teste obrigatório e imprimir a saída.

---

## Boas práticas exigidas

- Não colocar toda a lógica dentro do `main`
- Não usar atributos públicos
- Não misturar regra de negócio com impressão no terminal
- Evitar duplicação de código
- Usar nomes claros e sem abreviações confusas
- Separar responsabilidades
- Aplicar `Single Responsibility Principle`
- Aplicar `Open/Closed Principle`
- Aplicar `Dependency Inversion` quando fizer sentido, por exemplo, o `MotorRecomendacao` dependendo da interface `CriterioRecomendacao`
- Manter o código simples e legível

---

## Organização sugerida de pacotes

```text
src/
└── main/
    └── java/
        └── recomendacao/
            ├── app/
            │   └── Main.java
            ├── model/
            │   ├── Filme.java
            │   ├── PerfilUsuario.java
            │   ├── Recomendacao.java
            │   ├── Genero.java
            │   ├── Humor.java
            │   └── Classificacao.java
            ├── criterio/
            │   ├── CriterioRecomendacao.java
            │   ├── CriterioGenero.java
            │   ├── CriterioDuracao.java
            │   ├── CriterioClassificacao.java
            │   └── CriterioHumor.java
            ├── service/
            │   └── MotorRecomendacao.java
            └── repository/
                └── CatalogoFilmes.java
```

---

## Explicações obrigatórias

Também quero que você explique brevemente:

1. Como o **Strategy Pattern** foi aplicado.
2. Como o **Open/Closed Principle** aparece no projeto.
3. Como adicionar uma nova regra, por exemplo: “recomendar apenas filmes com nota IMDb maior que 7”, sem alterar as classes já existentes.
4. Por que essa implementação é realmente orientada a objetos e não apenas código imperativo escrito em Java.

---

## Entrega esperada

Entregue:

- Código Java completo
- Estrutura de pastas sugerida
- Explicação curta das principais classes
- Saída esperada do caso de teste obrigatório
