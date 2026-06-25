# Sistema de Recomendação de Filmes - Paradigma Lógico (Prolog)

## Versão utilizada

SWI-Prolog 9.2+

## Execução direta

Executar o programa pela raiz do projeto:

```bash
swipl -q -s logica/logica.pl
```

## Consulta manual

Tambem e possivel abrir o SWI-Prolog e consultar o predicado principal:

```prolog
?- [logica/logica].
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
* ordenação com predsort/3
