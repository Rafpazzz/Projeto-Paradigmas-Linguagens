# Paradigma Orientado a Objetos

## Versao do compilador

- Java: OpenJDK 21.0.11
- Compilador: `javac 21.0.11`

## Como compilar

Execute os comandos a partir da raiz do repositorio:

```bash
mkdir -p /tmp/projeto-paradigmas-oo-build
javac -d /tmp/projeto-paradigmas-oo-build $(find OO/src/main/java -name '*.java')
```

## Como executar

Execute a partir da raiz do repositorio:

```bash
java -cp /tmp/projeto-paradigmas-oo-build recomendacao.app.Main
```

O programa le os dados dos filmes no arquivo:

```text
OO/src/filmes
```

## Dependencias externas

Nao ha dependencias externas. O projeto usa apenas bibliotecas padrao do Java.
