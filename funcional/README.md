# Sistema de Recomendacao de Filmes em Haskell Funcional

Implementacao funcional do sistema de recomendacao de filmes. A logica de recomendacao foi escrita com funcoes puras, dados imutaveis e composicao de transformacoes sobre listas.

## Ambiente

- Linguagem: Haskell
- Compilador/interprete recomendado: GHC ou RunHaskell
- Dependencias externas: nenhuma

## Estrutura

```text
funcional/
  Main.hs
  README.md
```

## Passo a passo para executar

1. Abrir um terminal na pasta raiz do repositorio.

A pasta raiz e a que contem as pastas `OO/`, `funcional/`, `Iperativa/` e `logica/`.
Todos os comandos abaixo consideram esse ponto de partida.

O codigo Haskell desta implementacao esta no caminho relativo:

```text
funcional/Main.hs
```

2. Instalar o GHC e o Cabal, caso ainda nao estejam instalados:

No Fedora:

```bash
sudo dnf install -y ghc cabal-install
```

No Ubuntu/Debian:

```bash
sudo apt install ghc cabal-install
```

Em qualquer sistema, tambem e possivel instalar pelo GHCup:

```bash
curl --proto '=https' --tlsv1.2 -sSf https://get-ghcup.haskell.org -o /tmp/get-ghcup.sh
env BOOTSTRAP_HASKELL_NONINTERACTIVE=1 sh /tmp/get-ghcup.sh
. "$HOME/.ghcup/env"
```

No Windows, instalar pelo GHCup para Windows ou pelo Stack.

3. Conferir as versoes:

```bash
ghc --version
runhaskell --version
```

O comando `ghc --version` deve mostrar que o compilador esta instalado.

4. Executar diretamente com `runhaskell`:

```bash
runhaskell funcional/Main.hs
```

Esse comando interpreta e executa diretamente o arquivo `funcional/Main.hs`.

5. Opcionalmente, compilar com `ghc`:

```bash
ghc -outputdir /tmp/funcional-build -o /tmp/funcional funcional/Main.hs
/tmp/funcional
```

O primeiro comando compila `funcional/Main.hs` e gera o executavel em `/tmp/funcional`.
O segundo comando executa esse binario compilado.

## Regras

- O filme precisa ter pelo menos um genero favorito do usuario.
- A duracao do filme precisa ser menor ou igual a duracao maxima aceita.
- A classificacao etaria do filme precisa ser menor ou igual a classificacao permitida pelo usuario.
- A classificacao e representada por numeros: `0` para livre, `12`, `14` e `18`.
- O humor adiciona prioridade a generos especificos.
- Apenas filmes com pontuacao maior ou igual a 2 sao recomendados.

## Pontuacao

Para seguir a regra de pontuacao do enunciado, a pontuacao soma:

- `+1` para cada genero do filme que esteja nos generos favoritos do usuario.
- `+1` quando algum genero do filme coincide com os generos priorizados pelo humor.

Duracao e classificacao etaria funcionam como filtros, nao como pontuacao.
