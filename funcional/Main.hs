module Main where

import Data.List (intercalate, sortBy)

-- Classificacao e tratada como numero para facilitar a regra "livre <= 12 <= 14 <= 18".
-- O valor 0 representa a classificacao livre.
type Classificacao = Int

classificacaoLivre :: Classificacao
classificacaoLivre = 0

-- Humor tem apenas os tres valores aceitos pelo enunciado.
data Humor
    = Animado
    | Reflexivo
    | Triste
    deriving (Eq)

-- Filme representa cada item da base obrigatoria do trabalho.
data Filme = Filme
    { titulo :: String
    , generos :: [String]
    , duracaoMinutos :: Int
    , classificacao :: Classificacao
    }

-- PerfilUsuario representa a entrada usada para calcular as recomendacoes.
data PerfilUsuario = PerfilUsuario
    { generosFavoritos :: [String]
    , humor :: Humor
    , duracaoMaxima :: Int
    , classificacaoPermitida :: Classificacao
    }

-- Recomendacao guarda o filme junto da pontuacao calculada.
data Recomendacao = Recomendacao
    { filme :: Filme
    , pontuacaoFinal :: Int
    }

-- Base fixa de filmes exigida pelo enunciado. Os dados originais nao devem ser alterados.
catalogo :: [Filme]
catalogo =
    [ Filme "Duna" ["ficcao_cientifica"] 155 12
    , Filme "Vingadores" ["acao"] 149 12
    , Filme "Forrest Gump" ["drama"] 142 classificacaoLivre
    , Filme "Superbad" ["comedia"] 113 18
    , Filme "La La Land" ["romance"] 128 classificacaoLivre
    , Filme "Mad Max" ["acao"] 120 14
    , Filme "Her" ["ficcao_cientifica", "drama"] 126 14
    , Filme "Comer Rezar Amar" ["romance"] 133 classificacaoLivre
    , Filme "Tropa de Elite" ["acao", "drama"] 115 18
    , Filme "Interestelar" ["ficcao_cientifica"] 169 classificacaoLivre
    , Filme "Clueless" ["comedia", "romance"] 97 14
    , Filme "O Poderoso Chefão" ["drama"] 175 14
    ]

-- Entrada obrigatoria da secao 7 do enunciado.
perfilObrigatorio :: PerfilUsuario
perfilObrigatorio =
    PerfilUsuario
        { generosFavoritos = ["acao", "ficcao_cientifica"]
        , humor = Animado
        , duracaoMaxima = 150
        , classificacaoPermitida = 14
        }

-- Entrada adicional para testar outro humor, outros generos e classificacao maior.
perfilReflexivo :: PerfilUsuario
perfilReflexivo =
    PerfilUsuario
        { generosFavoritos = ["drama", "romance"]
        , humor = Reflexivo
        , duracaoMaxima = 180
        , classificacaoPermitida = 18
        }

-- Entrada adicional para demonstrar o comportamento quando a lista de generos vem vazia.
perfilSemGeneros :: PerfilUsuario
perfilSemGeneros =
    PerfilUsuario
        { generosFavoritos = []
        , humor = Triste
        , duracaoMaxima = 140
        , classificacaoPermitida = classificacaoLivre
        }

-- R3: a classificacao do filme precisa ser menor ou igual ao limite do usuario.
classifOk :: Classificacao -> Classificacao -> Bool
classifOk classificacaoUsuario classificacaoFilme =
    classificacaoFilme <= classificacaoUsuario

-- R4: mapeia cada humor para os generos priorizados por esse humor.
generosPorHumor :: Humor -> [String]
generosPorHumor Animado = ["acao", "comedia"]
generosPorHumor Reflexivo = ["drama", "ficcao_cientifica"]
generosPorHumor Triste = ["comedia", "romance"]

-- R1: verifica se o filme possui pelo menos um genero favorito do usuario.
temGeneroFavorito :: PerfilUsuario -> Filme -> Bool
temGeneroFavorito perfil =
    any (`elem` generosFavoritos perfil) . generos

-- Calcula a relevancia do filme para o perfil.
-- O bloco where separa os pedacos da conta sem criar estado mutavel.
pontuacao :: PerfilUsuario -> Filme -> Int
pontuacao perfil filmeAtual =
    bonusGeneroAtendido + pontosGenerosFavoritos + bonusHumor
  where
    -- +1 para cada genero do filme que aparece nos favoritos do usuario.
    pontosGenerosFavoritos =
        length (filter (`elem` generosFavoritos perfil) (generos filmeAtual))
    -- +1 quando a regra de genero foi satisfeita, mantendo equivalencia com o caso obrigatorio.
    bonusGeneroAtendido =
        if pontosGenerosFavoritos > 0 then 1 else 0
    -- +1 quando algum genero do filme coincide com os generos priorizados pelo humor.
    bonusHumor =
        if any (`elem` generosPorHumor (humor perfil)) (generos filmeAtual)
            then 1
            else 0

-- Funcao principal de recomendacao.
-- O operador (.) compoe as etapas: primeiro filtra, depois pontua, filtra o minimo e ordena.
recomenda :: PerfilUsuario -> [Filme] -> [Recomendacao]
recomenda perfil =
    sortBy compararRecomendacoes
        . filter ((>= 2) . pontuacaoFinal)
        . map (\filmeAtual -> Recomendacao filmeAtual (pontuacao perfil filmeAtual))
        . filter (classifOk (classificacaoPermitida perfil) . classificacao)
        . filter ((<= duracaoMaxima perfil) . duracaoMinutos)
        . filter (temGeneroFavorito perfil)

-- Ordena por pontuacao decrescente. Em empate, usa o titulo em ordem alfabetica.
compararRecomendacoes :: Recomendacao -> Recomendacao -> Ordering
compararRecomendacoes primeira segunda =
    case compare (pontuacaoFinal segunda) (pontuacaoFinal primeira) of
        EQ -> compare (titulo (filme primeira)) (titulo (filme segunda))
        resultado -> resultado

-- Transforma uma recomendacao em texto para exibir no terminal.
mostrarRecomendacao :: Recomendacao -> String
mostrarRecomendacao recomendacao =
    "[" ++ show (pontuacaoFinal recomendacao) ++ " pts] "
        ++ titulo filmeAtual
        ++ " ("
        ++ intercalate ", " (generos filmeAtual)
        ++ ", "
        ++ show (duracaoMinutos filmeAtual)
        ++ " min, "
        ++ show (classificacao filmeAtual)
        ++ ")"
  where
    -- Apelido local para evitar repetir "filme recomendacao" varias vezes.
    filmeAtual = filme recomendacao

-- Executa um cenario de teste e imprime suas recomendacoes.
imprimirCenario :: String -> PerfilUsuario -> IO ()
imprimirCenario nome perfil = do
    putStrLn nome
    putStrLn (replicate (length nome) '-')
    let recomendacoes = recomenda perfil catalogo
    if null recomendacoes
        then putStrLn "Nenhum filme recomendado."
        else mapM_ (putStrLn . mostrarRecomendacao) recomendacoes
    putStrLn ""

-- Ponto de entrada do programa. A logica de recomendacao fica pura; aqui ha apenas IO.
main :: IO ()
main = do
    imprimirCenario "Caso obrigatorio do enunciado" perfilObrigatorio
    imprimirCenario "Perfil reflexivo amplo" perfilReflexivo
    imprimirCenario "Perfil sem generos favoritos" perfilSemGeneros
