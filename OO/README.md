# Sistema de Recomendacao de Filmes em Java OO

Projeto didatico em Java puro para demonstrar Programacao Orientada a Objetos, encapsulamento, classes com responsabilidades bem definidas, Strategy Pattern e principios SOLID.

## Estrutura

```text
OO/
  src/main/java/recomendacao/
    app/
      Main.java
    criterio/
      CriterioRecomendacao.java
      CriterioGenero.java
      CriterioDuracao.java
      CriterioClassificacao.java
    model/
      ClassificacaoEtaria.java
      Filme.java
      FilmeRecomendado.java
      PerfilUsuario.java
    repository/
      CatalogoFilmes.java
    service/
      CalculadoraPontuacao.java
      MotorRecomendacao.java
```

## Como executar

Na raiz do repositorio:

```bash
javac -d /tmp/oo-build $(find OO/src/main/java -name '*.java')
java -cp /tmp/oo-build recomendacao.app.Main
```

## Regras

- O filme precisa ter pelo menos um genero favorito do usuario.
- A duracao do filme precisa ser menor ou igual a duracao maxima do usuario.
- A classificacao etaria do filme precisa ser permitida pela classificacao do usuario.
- O humor adiciona prioridade para generos especificos.
- Apenas filmes com pontuacao maior ou igual a 2 sao recomendados.

## Pontuacao

A `CalculadoraPontuacao` soma:

- `+1` para cada genero do filme que esteja nos generos favoritos do usuario.
- `+1` quando algum genero do filme coincide com os generos priorizados pelo humor.

Duracao e classificacao etaria sao filtros, nao pontuacao.
