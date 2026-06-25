filme('Duna',
      [ficcao_cientifica],
      155,
      12).

filme('Vingadores',
      [acao],
      149,
      12).

filme('Forrest Gump',
      [drama],
      142,
      livre).

filme('Superbad',
      [comedia],
      113,
      18).

filme('La La Land',
      [romance],
      128,
      livre).

filme('Mad Max',
      [acao],
      120,
      14).

filme('Her',
      [ficcao_cientifica, drama],
      126,
      14).

filme('Comer Rezar Amar',
      [romance],
      133,
      livre).

filme('Tropa de Elite',
      [acao, drama],
      115,
      18).

filme('Interestelar',
      [ficcao_cientifica],
      169,
      livre).

filme('Clueless',
      [comedia, romance],
      97,
      14).

filme('O Poderoso Chefao',
      [drama],
      175,
      14).

nivel(livre, 0).
nivel(12, 1).
nivel(14, 2).
nivel(18, 3).

classif_ok(Filme, Usuario) :-
    nivel(Filme, N1),
    nivel(Usuario, N2),
    N1 =< N2.

humor_genero(animado,
             [acao, comedia]).

humor_genero(reflexivo,
             [drama, ficcao_cientifica]).

humor_genero(triste,
             [comedia, romance]).

contar_generos([], _, 0).

contar_generos([G|R], Favoritos, P) :-
    member(G, Favoritos),
    contar_generos(R, Favoritos, P1),
    P is P1 + 1.

contar_generos([G|R], Favoritos, P) :-
    \+ member(G, Favoritos),
    contar_generos(R, Favoritos, P).

possui_genero_favorito(Generos, Favoritos) :-
    member(G, Generos),
    member(G, Favoritos).

bonus_humor(Generos, Humor, 1) :-
    humor_genero(Humor, Prioritarios),
    member(G, Generos),
    member(G, Prioritarios),
    !.

bonus_humor(_, _, 0).

pontuacao(Generos,
          Favoritos,
          Humor,
          Pontos) :-

    contar_generos(
        Generos,
        Favoritos,
        P1
    ),

    (
        possui_genero_favorito(
            Generos,
            Favoritos
        )
        ->
        BonusR1 = 1
        ;
        BonusR1 = 0
    ),

    bonus_humor(
        Generos,
        Humor,
        P2
    ),

    Pontos is P1 + BonusR1 + P2.

recomenda(Titulo,
          Humor,
          Favoritos,
          DuracaoMax,
          Classificacao) :-

    filme(Titulo,
          Generos,
          Duracao,
          ClassFilme),

    member(G, Generos),
    member(G, Favoritos),

    Duracao =< DuracaoMax,

    classif_ok(ClassFilme,
               Classificacao),

    pontuacao(Generos,
              Favoritos,
              Humor,
              Pontos),

    Pontos >= 2.

recomenda_filmes(Favoritos,
                 Humor,
                 DuracaoMax,
                 Classificacao,
                 Resultado) :-

    findall(
        P-Titulo,
        (
            filme(Titulo,
                  Generos,
                  Duracao,
                  ClassFilme),

            member(G, Generos),
            member(G, Favoritos),

            Duracao =< DuracaoMax,

            classif_ok(ClassFilme,
                       Classificacao),

            pontuacao(
                Generos,
                Favoritos,
                Humor,
                P
            ),

            P >= 2
        ),
        Lista
    ),

    keysort(Lista, Ordenada),
    reverse(Ordenada, Resultado).