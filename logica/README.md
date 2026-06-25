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
   recomenda_filmes([acao, ficcao_cientifica],
                    animado,
                    150,
                    14,
                    Aluno1),
   writeln(Aluno1),

   recomenda_filmes([drama, romance],
                    reflexivo,
                    180,
                    18,
                    Aluno2),
   writeln(Aluno2),

   recomenda_filmes([],
                    triste,
                    140,
                    livre,
                    Aluno3),
   writeln(Aluno3).
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
