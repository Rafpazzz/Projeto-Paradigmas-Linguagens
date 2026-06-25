# Sistema de Recomendação de Filmes - Paradigma Lógico (Prolog)

## Versão utilizada

SWI-Prolog 9.2+

## Execução

Abrir o SWI-Prolog:

```bash
swipl
```

Carregar o programa:

```prolog
?- [recomendador].
```

Executar a consulta:

```prolog
?- recomenda_filmes(
       [acao, ficcao_cientifica],
       animado,
       150,
       14,
       Resultado
   ).
```

## Dependências

Nenhuma dependência externa.

## Observações

A implementação segue o paradigma lógico puro:

* fatos filme/4
* regras declarativas
* sem assertz/retract
* uso de findall/3
* ordenação com keysort/2
