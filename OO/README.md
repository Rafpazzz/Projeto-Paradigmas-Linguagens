# Sistema de Recomendacao de Filmes em Java OO

Projeto didatico em Java puro para demonstrar Programacao Orientada a Objetos, encapsulamento, classes com responsabilidades bem definidas, Strategy Pattern e principios SOLID.

## Ambiente

- Linguagem: Java
- Versao da linguagem usada: Java 21
- Compilador utilizado: `javac 21.0.11`
- Interpretador/runtime utilizado: `java 21.0.11`
- Dependencias externas: nenhuma

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

## Passo a passo para executar

1. Abrir um terminal na pasta raiz do repositorio.

A pasta raiz e a que contem as pastas `OO/`, `funcional/`, `Iperativa/` e `logica/`.
Todos os comandos abaixo consideram esse ponto de partida.

O codigo Java desta implementacao esta no caminho relativo:

```text
OO/src/main/java/recomendacao/
```

O arquivo principal esta em:

```text
OO/src/main/java/recomendacao/app/Main.java
```

2. Instalar o Java 21, caso ainda nao esteja instalado:

No Fedora:

```bash
sudo dnf install -y java-21-openjdk java-21-openjdk-devel
```

No Ubuntu/Debian:

```bash
sudo apt install openjdk-21-jdk
```

No Windows, instalar o JDK 21 pelo site da Oracle, Eclipse Temurin ou Microsoft Build of OpenJDK.

3. Conferir as versoes:

```bash
java -version
javac -version
```

O comando `javac -version` deve indicar uma versao 21 ou superior.

4. Compilar o codigo Java:

```bash
javac -d /tmp/oo-build $(find OO/src/main/java -name '*.java')
```

Esse comando procura todos os arquivos `.java` dentro de `OO/src/main/java/` e gera os arquivos compilados em `/tmp/oo-build`.

No Windows PowerShell, usar:

```powershell
$files = Get-ChildItem -Recurse OO/src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -d "$env:TEMP\oo-build" $files
```

5. Executar o programa:

```bash
java -cp /tmp/oo-build recomendacao.app.Main
```

Esse comando executa a classe principal `recomendacao.app.Main`, que esta no arquivo `OO/src/main/java/recomendacao/app/Main.java`.

No Windows PowerShell, usar:

```powershell
java -cp "$env:TEMP\oo-build" recomendacao.app.Main
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
