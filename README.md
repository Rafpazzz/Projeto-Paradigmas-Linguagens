# Projeto Paradigmas de Linguagens

Sistema de recomendacao de filmes implementado em paradigmas diferentes para comparacao academica.

## Estrutura atual

```text
OO/          Implementacao orientada a objetos em Java
funcional/  Implementacao funcional em Haskell
Iperativa/  Espaco reservado para a implementacao imperativa em C
logica/     Espaco reservado para a implementacao logica em Prolog
```

## Como executar

Depois de baixar ou clonar o repositorio, abra um terminal na pasta raiz do projeto. A pasta raiz e a que contem este `README.md`.

Cada implementacao deve ser executada pelos comandos descritos no README da sua propria pasta:

1. Java OO: veja `OO/README.md`.
2. Haskell funcional: veja `funcional/README.md`.
3. C imperativo: pendente de integracao.
4. Prolog logico: pendente de integracao.

## Caso obrigatorio

Entrada usada no caso obrigatorio do enunciado:

```text
generos_favoritos: acao, ficcao_cientifica
humor: animado
duracao_max: 150
classificacao: 14
```

Saida esperada:

```text
[3 pts] Mad Max
[3 pts] Vingadores
[2 pts] Her
```

Mad Max e Vingadores podem trocar de ordem por terem a mesma pontuacao.
