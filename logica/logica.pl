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

filme('O Poderoso Chefão',
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

    bonus_humor(
        Generos,
        Humor,
        P2
    ),

    Pontos is P1 + P2.

recomenda(Titulo,
          Humor,
          Favoritos,
          DuracaoMax,
          Classificacao) :-

    filme(Titulo,
          Generos,
          Duracao,
          ClassFilme),

    possui_genero_favorito(Generos,
                            Favoritos),

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

    resultados_ordenados(
        Favoritos,
        Humor,
        DuracaoMax,
        Classificacao,
        Ordenados
    ),

    maplist(resultado_par, Ordenados, Resultado).

resultado_par(resultado(Pontos, Titulo, _, _, _), Pontos-Titulo).

resultado_recomendado(Favoritos,
                      Humor,
                      DuracaoMax,
                      Classificacao,
                      resultado(P, Titulo, Generos, Duracao, ClassFilme)) :-

    filme(Titulo,
          Generos,
          Duracao,
          ClassFilme),

    possui_genero_favorito(Generos,
                            Favoritos),

    Duracao =< DuracaoMax,

    classif_ok(ClassFilme,
               Classificacao),

    pontuacao(
        Generos,
        Favoritos,
        Humor,
        P
    ),

    P >= 2.

comparar_resultados(Ordem,
                    resultado(P1, T1, _, _, _),
                    resultado(P2, T2, _, _, _)) :-

    compare(ComparacaoPontos, P2, P1),
    (
        ComparacaoPontos = (=)
        ->
        compare(Ordem, T1, T2)
        ;
        Ordem = ComparacaoPontos
    ).

resultados_ordenados(Favoritos,
                     Humor,
                     DuracaoMax,
                     Classificacao,
                     Ordenados) :-

    findall(
        Resultado,
        resultado_recomendado(
            Favoritos,
            Humor,
            DuracaoMax,
            Classificacao,
            Resultado
        ),
        Lista
    ),

    predsort(comparar_resultados, Lista, Ordenados).

texto_humor(animado, 'Animado').
texto_humor(reflexivo, 'Reflexivo').
texto_humor(triste, 'Triste').

texto_classificacao(livre, livre).

texto_classificacao(Classificacao, Texto) :-
    number(Classificacao),
    atom_number(Texto, Classificacao).

texto_generos([], nenhum).

texto_generos(Generos, Texto) :-
    Generos \= [],
    atomic_list_concat(Generos, ', ', Texto).

imprimir_tracos(0) :-
    nl.

imprimir_tracos(Quantidade) :-
    Quantidade > 0,
    write('-'),
    Proxima is Quantidade - 1,
    imprimir_tracos(Proxima).

imprimir_resultado(resultado(Pontos, Titulo, Generos, Duracao, Classificacao)) :-
    texto_generos(Generos, GenerosTexto),
    texto_classificacao(Classificacao, ClassificacaoTexto),
    format('[~w pts] ~a (~a, ~w min, ~a)~n',
           [Pontos, Titulo, GenerosTexto, Duracao, ClassificacaoTexto]).

imprimir_resultados([]) :-
    writeln('Nenhum filme recomendado.').

imprimir_resultados([Resultado|Restante]) :-
    imprimir_resultado(Resultado),
    imprimir_resultados_restantes(Restante).

imprimir_resultados_restantes([]).

imprimir_resultados_restantes([Resultado|Restante]) :-
    imprimir_resultado(Resultado),
    imprimir_resultados_restantes(Restante).

imprimir_cenario(Nome,
                 Favoritos,
                 Humor,
                 DuracaoMax,
                 Classificacao) :-

    texto_generos(Favoritos, GenerosTexto),
    texto_humor(Humor, HumorTexto),
    texto_classificacao(Classificacao, ClassificacaoTexto),
    format(string(Cabecalho),
           '~a (generos: ~a | humor: ~a | duracao max: ~w min | classificacao: ~a)',
           [Nome, GenerosTexto, HumorTexto, DuracaoMax, ClassificacaoTexto]),

    format('~s~n', [Cabecalho]),
    string_length(Cabecalho, Tamanho),
    imprimir_tracos(Tamanho),

    resultados_ordenados(
        Favoritos,
        Humor,
        DuracaoMax,
        Classificacao,
        Resultados
    ),

    imprimir_resultados(Resultados),
    nl.

main :-
    imprimir_cenario(
        'USUARIO 01',
        [acao, ficcao_cientifica],
        animado,
        150,
        14
    ),

    imprimir_cenario(
        'USUARIO 02',
        [drama, romance],
        reflexivo,
        180,
        18
    ),

    imprimir_cenario(
        'USUARIO 03',
        [],
        triste,
        140,
        livre
    ).

:- initialization(main, main).
