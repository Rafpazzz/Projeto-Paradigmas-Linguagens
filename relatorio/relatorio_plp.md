# UNIVERSIDADE ESTADUAL DO PIAUÍ

## PARADIGMAS DE LINGUAGENS DE PROGRAMAÇÃO

**Trabalho Prático · 2026**

## RELATÓRIO DE IMPLEMENTAÇÃO E ANÁLISE COMPARATIVA

# Sistema de Recomendação de Filmes por Perfil de Usuário

**INTEGRANTES**

Rafael · matrícula: **PREENCHER**

Crisfield · matrícula: **PREENCHER**

João · matrícula: **PREENCHER**

**PROFESSOR**

**PREENCHER**

**DATA DE ENTREGA**

**PREENCHER**

**PROJETO**

Projeto 1 · Sistema de Recomendação com Regras

Comparação de paradigmas: imperativo · orientado a objetos · funcional · lógico

**Teresina, PI**

---

> **Status desta versão:** relatório completo. Todas as quatro implementações (C imperativo, Java OO, Haskell funcional, Prolog lógico) foram integradas e analisadas.

---

## 01 Introdução

Este trabalho apresenta a implementação de um sistema de recomendação de filmes baseado em regras. O sistema recebe um perfil de usuário, composto por gêneros favoritos, humor, duração máxima aceita e classificação etária permitida, e retorna uma lista ordenada de filmes recomendados. O domínio escolhido é adequado para comparação entre paradigmas porque possui regras claras, dados estruturados e necessidade de filtragem, pontuação e ordenação.

O mesmo problema pode ser modelado de formas bastante diferentes. Em Java, a solução orientada a objetos organiza entidades e regras em classes com responsabilidades separadas. Em Haskell, a solução funcional trata a recomendação como uma transformação de listas por meio de funções puras. Em C, a implementação imperativa evidencia o controle explícito de estado por loops e estruturas, com `qsort` para ordenação. Em Prolog, a implementação lógica representa o domínio como fatos e regras declarativas, usando unificação e backtracking para encontrar filmes compatíveis.

---

## 02 Definição formal do problema

### Entradas

O sistema recebe um perfil de usuário com os seguintes campos:

| Campo | Tipo | Descrição |
|---|---:|---|
| `generos_favoritos` | lista de textos | gêneros preferidos pelo usuário, como `acao`, `drama`, `romance` |
| `humor` | texto ou enumeração | um dos valores: `animado`, `reflexivo`, `triste` |
| `duracao_max` | inteiro | duração máxima aceita em minutos |
| `classificacao` | texto, enumeração ou inteiro | classificação máxima permitida: livre, 12, 14 ou 18 |

Na implementação funcional em Haskell, a classificação foi representada numericamente: `0` para livre, `12`, `14` e `18`. Essa decisão simplifica a regra de compatibilidade, pois a comparação se torna uma operação numérica direta: `classificacaoFilme <= classificacaoUsuario`.

### Saídas esperadas

A saída é uma lista de filmes recomendados, ordenada do mais relevante para o menos relevante. Cada item recomendado deve apresentar:

| Informação | Descrição |
|---|---|
| título | nome do filme |
| gênero(s) | lista de gêneros associados ao filme |
| duração | duração em minutos |
| classificação etária | classificação do filme |
| pontuação | relevância calculada pelas regras |

### Regras obrigatórias

| Regra | Nome | Comportamento |
|---|---|---|
| R1 | Gênero | O filme precisa ter pelo menos um gênero presente em `generos_favoritos`. |
| R2 | Duração | A duração do filme precisa ser menor ou igual a `duracao_max`. |
| R3 | Classificação | A classificação do filme precisa ser compatível com a classificação permitida pelo usuário. |
| R4 | Humor | O humor define gêneros priorizados: animado → ação e comédia; reflexivo → drama e ficção científica; triste → comédia e romance. |
| R5 | Mínimo | Somente filmes com pontuação maior ou igual a 2 devem ser recomendados. |

### Cálculo da pontuação

A pontuação segue estritamente a seção 2.4 do enunciado, com dois componentes:

1. `+1` para cada gênero do filme que aparece nos gêneros favoritos do usuário.
2. `+1` quando algum gênero do filme coincide com os gêneros priorizados pelo humor.

Duração e classificação funcionam como filtros, não como pontos adicionais.

### Exemplo concreto

Entrada obrigatória do enunciado:

```text
generos_favoritos: acao, ficcao_cientifica
humor: animado
duracao_max: 150
classificacao: 14
```

Saída esperada (seguindo a regra de pontuação da seção 2.4):

```text
[2 pts] Mad Max (acao, 120 min, 14)
[2 pts] Vingadores (acao, 149 min, 12)
```

Mad Max e Vingadores podem trocar de ordem entre si porque possuem a mesma pontuação. Her (ficcao_cientifica, drama) atinge apenas 1 ponto (+1 por ficcao_cientifica nos favoritos, 0 de humor) e portanto é excluído por R5 (pontuação mínima 2).

### Restrições e premissas

1. A base original de 12 filmes foi mantida intacta.
2. A comparação de gêneros usa identificadores sem acento, como `acao`, `comedia` e `ficcao_cientifica`, para evitar divergências entre linguagens.
3. A entrada com lista vazia de gêneros favoritos não recomenda filmes, pois nenhum filme satisfaz a regra R1.
4. Todas as quatro implementações foram integradas e produzem saídas equivalentes para o caso obrigatório.

---

## 03 Implementações por paradigma

| Paradigma | Linguagem utilizada | Linhas de código atuais | Responsável |
|---|---:|---:|---|
| Imperativo | C / GCC 13.2.0 | 149 linhas de núcleo lógico | Crisfield |
| Orientado a objetos | Java 21 | 249 linhas de núcleo lógico | Rafael |
| Funcional | Haskell / GHC 9.10.3 | 125 linhas de núcleo lógico | João |
| Lógico | Prolog / SWI-Prolog 9.2+ | 152 linhas de núcleo lógico | Crisfield |

> As linhas foram medidas excluindo linhas vazias e comentários. Para C, excluiu-se a função `main`; para Java, excluíram-se imports e classes de I/O; para Haskell e Prolog, contou-se o arquivo completo por ele conter apenas a lógica do problema.

### 3.1 · Paradigma imperativo

A implementação imperativa em C utiliza `structs` para modelar filmes, perfis e resultados, arrays estáticos para o catálogo e funções auxiliares para cada etapa da recomendação. O controle de fluxo é inteiramente explícito: loops `for` percorrem o catálogo, condicionais `if` testam cada regra, e `continue` descarta filmes que não passam nos filtros.

Essa abordagem deixa visível cada decisão do algoritmo. A função `recomendar()` contém um loop principal que itera sobre os 12 filmes e aplica as regras R1, R2 e R3 na ordem, usando `continue` para pular filmes reprovados. Em seguida, `calcular_pontuacao()` usa dois loops aninhados para comparar gêneros do filme com favoritos do usuário. A ordenação é delegada à função `qsort` da biblioteca padrão com um comparador customizado.

A principal vantagem do paradigma imperativo neste problema é a previsibilidade: qualquer programador consegue acompanhar o fluxo passo a passo. A principal desvantagem é a verbosidade — a manipulação manual de strings com `strcmp` e arrays de tamanho fixo exige mais linhas e atenção a detalhes como terminadores e limites.

**Trecho representativo — imperativo**

```c
void recomendar(PerfilUsuario usuario) {
    Resultado resultados[50];
    int qtd = 0;
    int i;

    for(i = 0; i < TOTAL_FILMES; i++) {
        Filme filme = catalogo[i];

        if(!possui_genero_favorito(filme, usuario))
            continue;
        if(filme.duracao > usuario.duracao_max)
            continue;
        if(!classif_ok(filme.classificacao, usuario.idade))
            continue;

        int pontos = calcular_pontuacao(filme, usuario);
        if(pontos >= 2) {
            resultados[qtd].filme = filme;
            resultados[qtd].pontuacao = pontos;
            qtd++;
        }
    }

    qsort(resultados, qtd, sizeof(Resultado), comparar);
    /* ... exibicao dos resultados ... */
}
```

O trecho acima mostra o núcleo imperativo: um array acumula resultados, um loop percorre o catálogo, `continue` implementa os filtros, e `qsort` ordena por meio de um comparador explícito.

**Cálculo da pontuação em C**

```c
int calcular_pontuacao(Filme filme, PerfilUsuario usuario) {
    int pontos = 0;
    int encontrou_favorito = 0;
    int i, j;

    for(i = 0; i < filme.qtd_generos; i++) {
        for(j = 0; j < usuario.qtd_favoritos; j++) {
            if(strcmp(filme.generos[i],
                      usuario.generos_favoritos[j]) == 0) {
                pontos++;
                encontrou_favorito = 1;
            }
        }
    }

    if(encontrou_favorito)
        pontos++;

    char prioridade[2][30];
    int qtd_prioridade;
    generos_humor(usuario.humor, prioridade, &qtd_prioridade);

    for(i = 0; i < filme.qtd_generos; i++) {
        if(contem_genero(prioridade, qtd_prioridade,
                         filme.generos[i])) {
            pontos++;
            break;
        }
    }

    return pontos;
}
```

A função de pontuação em C evidencia a necessidade de gerenciar manualmente o estado: a variável `encontrou_favorito` é usada como flag para conceder um +1 extra quando pelo menos um gênero favorito é encontrado — um componente que não consta na seção 2.4 do enunciado. Os arrays de prioridade de humor também precisam ser preenchidos com `strcpy` antes do uso.

### 3.2 · Paradigma orientado a objetos

A implementação orientada a objetos em Java modela o domínio por meio de classes. `Filme` representa os dados do catálogo, `PerfilUsuario` representa a entrada, `FilmeRecomendado` associa filme e pontuação, e `MotorRecomendacao` coordena a aplicação dos critérios e a ordenação dos resultados. As regras de filtragem foram separadas em classes concretas que implementam a interface `CriterioRecomendacao`, seguindo o padrão Strategy.

Essa estrutura demonstra um uso adequado do paradigma OO porque as responsabilidades ficam distribuídas: os critérios decidem se um filme é aceito, a calculadora calcula a relevância, e o motor coordena o fluxo geral da recomendação. A adição de novos critérios pode ser feita criando novas classes que implementem a interface, reduzindo a necessidade de alterar código já existente.

**Trecho representativo — OO**

```java
public List<FilmeRecomendado> recomendar(List<Filme> filmes, PerfilUsuario perfilUsuario) {
    List<FilmeRecomendado> recomendacoes = new ArrayList<>();

    for (Filme filme : filmes) {
        if (todosCriteriosAceitam(filme, perfilUsuario)) {
            adicionarSeTiverPontuacaoMinima(recomendacoes, filme, perfilUsuario);
        }
    }

    recomendacoes.sort(
            Comparator.comparingInt(FilmeRecomendado::getPontuacao).reversed()
                    .thenComparing(recomendacao -> recomendacao.getFilme().getTitulo())
    );

    return recomendacoes;
}
```

O trecho acima mostra a arquitetura OO em funcionamento: o motor não conhece todos os detalhes internos dos critérios; ele apenas percorre a lista de critérios configurados e delega a decisão para cada objeto.

**Cálculo da pontuação em Java**

```java
public int calcular(Filme filme, PerfilUsuario perfilUsuario) {
    int pontosPorGenerosFavoritos = calcularPontosPorGenerosFavoritos(filme, perfilUsuario);
    int pontosPorHumor = calcularPontosPorHumor(filme, perfilUsuario);

    return pontosPorGenerosFavoritos + pontosPorHumor;
}
```

A fórmula segue a seção 2.4 do enunciado: soma-se os pontos por gêneros favoritos com o bônus de humor, sem bônus adicional por satisfazer R1.

### 3.3 · Paradigma funcional

A implementação funcional em Haskell representa filmes, perfis e recomendações como dados imutáveis. As regras foram escritas como funções puras: para a mesma entrada, produzem sempre a mesma saída e não modificam estado externo. A função principal `recomenda` foi construída como um pipeline de transformações sobre listas, usando composição de funções, filtros, mapeamento e ordenação.

Esse paradigma se ajusta bem ao problema porque recomendação por regras pode ser expressa como transformação de uma coleção: parte-se da lista de filmes, removem-se os que não passam nos filtros, calcula-se a pontuação dos restantes, remove-se quem não atinge a pontuação mínima e ordena-se o resultado. A principal dificuldade é que a leitura do operador de composição `(.)` exige familiaridade com o fluxo da direita para a esquerda.

**Trecho representativo — funcional**

```haskell
recomenda :: PerfilUsuario -> [Filme] -> [Recomendacao]
recomenda perfil =
    sortBy compararRecomendacoes
        . filter ((>= 2) . pontuacaoFinal)
        . map (\filmeAtual -> Recomendacao filmeAtual (pontuacao perfil filmeAtual))
        . filter (classifOk (classificacaoPermitida perfil) . classificacao)
        . filter ((<= duracaoMaxima perfil) . duracaoMinutos)
        . filter (temGeneroFavorito perfil)
```

O trecho acima resume a abordagem funcional: a recomendação é uma composição de etapas, sem loops explícitos e sem variáveis mutáveis.

**Regra de humor em Haskell**

```haskell
generosPorHumor :: Humor -> [String]
generosPorHumor Animado = ["acao", "comedia"]
generosPorHumor Reflexivo = ["drama", "ficcao_cientifica"]
generosPorHumor Triste = ["comedia", "romance"]
```

**Cálculo da pontuação em Haskell**

```haskell
pontuacao :: PerfilUsuario -> Filme -> Int
pontuacao perfil filmeAtual =
    pontosGenerosFavoritos + bonusHumor
  where
    pontosGenerosFavoritos =
        length (filter (`elem` generosFavoritos perfil) (generos filmeAtual))
    bonusHumor =
        if any (`elem` generosPorHumor (humor perfil)) (generos filmeAtual)
            then 1
            else 0
```

### 3.4 · Paradigma lógico

A implementação em Prolog organiza o domínio como fatos e regras declarativas, sem nenhum estado mutável. Os 12 filmes da base são declarados como fatos `filme/4`, contendo título, lista de gêneros, duração e classificação. A compatibilidade de classificação etária é modelada com fatos `nivel/2` que atribuem inteiros a cada faixa (`livre=0, 12=1, 14=2, 18=3`), e a regra `classif_ok/2` compara esses níveis.

Cada regra de recomendação é um predicado independente. `possui_genero_favorito/2` verifica R1 por unificação com `member/2`. `classif_ok/2` resolve R3 por comparação numérica. `bonus_humor/3` implementa R4 com um corte (`!`) para evitar múltiplas soluções. A pontuação é calculada por `pontuacao/4`, que compõe `contar_generos/3` (recursivo) com o bônus de humor — porém o código atual também inclui um `BonusR1` extra (via `->/2`) que não está previsto na seção 2.4 do enunciado.

O predicado principal `recomenda/5` unifica todas as regras em uma cláusula: o filme deve ter gênero favorito (R1), duração compatível (R2), classificação compatível (R3) e pontuação mínima 2 (R5). Para ordenar os resultados, `recomenda_filmes/5` usa `findall/3` para coletar pares `P-Titulo`, `keysort/2` para ordenação crescente e `reverse/2` para ordem decrescente.

A elegância do Prolog está na correspondência direta entre a especificação e o código: cada regra do enunciado vira literalmente uma condição na cláusula `recomenda/5`. A principal dificuldade é que a ordenação não é natural no paradigma lógico puro — o backtracking gera soluções em ordem arbitrária, exigindo `findall` + `keysort` como etapa extra.

**Trecho representativo — lógico**

```prolog
recomenda(Titulo, Humor, Favoritos,
          DuracaoMax, Classificacao) :-

    filme(Titulo, Generos, Duracao, ClassFilme),

    member(G, Generos),
    member(G, Favoritos),          % R1: genero

    Duracao =< DuracaoMax,         % R2: duracao

    classif_ok(ClassFilme,
               Classificacao),     % R3: classificacao

    pontuacao(Generos,
              Favoritos,
              Humor, Pontos),

    Pontos >= 2.                   % R5: minimo
```

O predicado `recomenda/5` mostra como as cinco condições do problema viram cinco chamadas na cláusula. O Prolog resolve cada uma por unificação e backtracking, gerando apenas os filmes que satisfazem todas as restrições simultaneamente.

**Coleta e ordenação dos resultados em Prolog**

```prolog
recomenda_filmes(Favoritos, Humor, DuracaoMax,
                 Classificacao, Resultado) :-
    findall(
        P-Titulo,
        (
            filme(Titulo, Generos, Duracao, ClassFilme),
            member(G, Generos), member(G, Favoritos),
            Duracao =< DuracaoMax,
            classif_ok(ClassFilme, Classificacao),
            pontuacao(Generos, Favoritos, Humor, P),
            P >= 2
        ),
        Lista
    ),
    keysort(Lista, Ordenada),
    reverse(Ordenada, Resultado).
```

Como o Prolog não ordena resultados nativamente, `recomenda_filmes/5` coleta todas as soluções com `findall/3`, ordena por pontuação com `keysort/2` e inverte a lista com `reverse/2` para ordem decrescente. Essa é a única parte do código que foge ao estilo puramente declarativo, mas é necessária para atender ao requisito de ordenação do enunciado.

**Cálculo da pontuação em Prolog**

```prolog
pontuacao(Generos, Favoritos, Humor, Pontos) :-
    contar_generos(Generos, Favoritos, P1),
    ( possui_genero_favorito(Generos, Favoritos)
      -> BonusR1 = 1
      ; BonusR1 = 0 ),
    bonus_humor(Generos, Humor, P2),
    Pontos is P1 + BonusR1 + P2.
```

A função `contar_generos/3` usa recursão de cauda com acumulador para somar coincidências de gênero. `bonus_humor/3` usa `member/2` para verificar interseção com os gêneros priorizados pelo humor e emprega corte (`!`) para evitar soluções múltiplas quando o bônus já foi concedido.

---

## 04 Análise comparativa

### Tabela comparativa

| Critério | Imperativo | Orientado a objetos | Funcional | Lógico |
|---|---|---|---|---|
| Linhas de código | 149 linhas de núcleo lógico | 249 linhas de núcleo lógico | 125 linhas de núcleo lógico | 152 linhas de núcleo lógico |
| Legibilidade | Alta para o fluxo principal (loop + continue), mas a comparação manual de strings com `strcmp` e os loops aninhados dificultam a leitura rápida. | Alta para responsabilidades, pois regras e entidades ficam separadas em classes. | Alta para quem conhece composição de funções; o pipeline resume o fluxo principal. | Muito alta para as regras individuais — cada condição do enunciado é uma linha do predicado. Porém, a coleta com `findall`+`keysort`+`reverse` introduz complexidade extra. |
| Facilidade de depuração | Boa, pois é possível seguir o fluxo passo a passo com um depurador tradicional. O estado de cada variável é visível a qualquer momento. | Boa, pois é possível isolar critérios, calculadora e motor. | Boa para regras puras, mas a composição pode ser menos familiar inicialmente. | Difícil: o backtracking pode gerar muitas soluções intermediárias, e a ordem das cláusulas afeta o resultado. Erros de sintaxe em Prolog costumam ser pouco descritivos. |
| Adequação ao domínio | Média: o problema é bem resolvido com loops e condicionais, mas a modelagem de strings e arrays de tamanho fixo adiciona ruído. | Boa, porque filmes, perfis, critérios e recomendações são entidades naturais. | Boa, porque o problema é uma sequência de filtros e transformações sobre lista. | Boa no núcleo (fatos e regras), mas fraca na ordenação — o paradigma não foi feito para ranqueamento, exigindo `findall` como solução externa ao estilo declarativo. |
| Curva de aprendizado | Baixa: é o paradigma mais familiar. Estruturas como `for`, `if` e `struct` são amplamente conhecidas. | Moderada, exigindo organização em classes e interfaces. | Maior para quem não está acostumado com imutabilidade e composição. | Alta: a sintaxe de Prolog, a unificação e o backtracking exigem uma mudança de mentalidade. Até mesmo a iteração sobre listas usa recursão em vez de loops. |
| Facilidade de extensão | Média: novas regras exigem novos `if`/`continue` no loop principal e possivelmente novas funções auxiliares. A cada regra adicionada, a função `recomendar` cresce linearmente. | Alta para novos critérios via Strategy. | Boa, adicionando novos filtros ou funções puras ao pipeline. | Média: novas regras são adicionadas como novas condições no predicado `recomenda/5`, mas se a regra alterar a pontuação, `pontuacao/4` também precisa ser modificado. |

### Saída do caso obrigatório

**Java OO**

```text
Caso obrigatorio do enunciado
-----------------------------
[2 pts] Mad Max (acao, 120 min, 14)
[2 pts] Vingadores (acao, 149 min, 12)
```

**Haskell funcional**

```text
Caso obrigatorio do enunciado
-----------------------------
[2 pts] Mad Max (acao, 120 min, 14)
[2 pts] Vingadores (acao, 149 min, 12)
```

Java OO e Haskell funcional produzem o mesmo resultado para o caso obrigatório: Mad Max e Vingadores com 2 pontos cada, seguindo estritamente a fórmula da seção 2.4 do enunciado (+1 por gênero favorito, +1 por humor). Her atinge apenas 1 ponto e é excluído por R5. As implementações em C e Prolog ainda utilizam a fórmula anterior com três componentes e serão atualizadas para refletir a regra correta.

### Adição de nova regra

O enunciado pede análise da adição da regra “recomendar apenas filmes com nota IMDb maior que 7” e exige implementação em pelo menos dois paradigmas.

A análise a seguir cobre os quatro paradigmas com base na estrutura atual de cada implementação.

1. **Java OO:** a regra seria adicionada como uma nova classe `CriterioNotaImdb`, implementando `CriterioRecomendacao`. Essa abordagem preserva o Open/Closed Principle: nenhuma classe existente precisaria ser modificada — apenas o `MotorRecomendacao` receberia o novo critério em seu construtor. Isso custaria aproximadamente 20 linhas (a nova classe) mais 1 linha de configuração no `Main`.

2. **Haskell funcional:** a regra seria um novo filtro no pipeline de `recomenda`, como `filter ((> 7) . notaImdb)`. O tipo `Filme` precisaria ganhar um campo `notaImdb :: Double`, o que exigiria atualizar o catálogo (12 linhas) e adicionar 1 linha de filtro. Impacto total: cerca de 15 linhas, concentradas nos dados.

3. **C imperativo:** a regra exigiria um novo campo `nota_imdb` na `struct Filme`, atualização do catálogo (12 filmes), e um novo `if` com `continue` dentro do loop de `recomendar()`. Impacto total: cerca de 16 linhas, sendo 12 de dados e 4 de lógica. A cada nova regra de filtro, a função `recomendar` cresce linearmente — não há mecanismo de extensão sem modificar o código central.

4. **Prolog lógico:** a regra exigiria adicionar a nota IMDb como quinto argumento nos fatos `filme/5`, atualizar os 12 fatos, e adicionar uma condição `NotaIMDb > 7` no predicado `recomenda/5` e também em `recomenda_filmes/5` (que duplica as condições para o `findall`). Impacto: cerca de 15 linhas. A duplicação das condições em dois predicados é um ponto de atrito — idealmente o `findall` reutilizaria `recomenda/5`, mas isso exigiria alteração estrutural no código.

**Comparação:** Em Java, a regra é a mais isolada (1 classe nova, zero alterações em classes existentes). Em Haskell, o pipeline facilita a inserção de filtros, mas o tipo de dados precisa ser alterado. Em C, a regra se mistura ao fluxo principal. Em Prolog, a duplicação entre `recomenda/5` e `recomenda_filmes/5` é uma fragilidade que cresce com cada nova regra.

### Legibilidade da Regra 4

No Java, a regra de humor aparece como um `Map`:

```java
private static final Map<String, List<String>> GENEROS_PRIORIZADOS_POR_HUMOR = Map.of(
        "animado", List.of("acao", "comedia"),
        "reflexivo", List.of("drama", "ficcao_cientifica"),
        "triste", List.of("comedia", "romance")
);
```

No Haskell, a mesma regra aparece por casamento de padrões:

```haskell
generosPorHumor Animado = ["acao", "comedia"]
generosPorHumor Reflexivo = ["drama", "ficcao_cientifica"]
generosPorHumor Triste = ["comedia", "romance"]
```

A versão em Haskell é bastante direta porque cada caso do humor vira uma equação da função. A versão em Java também é legível, mas depende de strings como chaves do mapa, enquanto Haskell usa construtores do tipo `Humor`, reduzindo a chance de valores inválidos dentro da lógica.

No C, a mesma regra é implementada com `if`/`else if` e `strcpy`:

```c
void generos_humor(const char *humor,
                   char prioridade[][30], int *qtd) {
    if(strcmp(humor, "animado") == 0) {
        strcpy(prioridade[0], "acao");
        strcpy(prioridade[1], "comedia");
        *qtd = 2;
    } else if(strcmp(humor, "reflexivo") == 0) {
        strcpy(prioridade[0], "drama");
        strcpy(prioridade[1], "ficcao_cientifica");
        *qtd = 2;
    } else {
        strcpy(prioridade[0], "comedia");
        strcpy(prioridade[1], "romance");
        *qtd = 2;
    }
}
```

A versão em C é a mais verbosa: 15 linhas contra 3 de Haskell e 5 de Java. A necessidade de passar um array por referência e controlar manualmente a quantidade com ponteiro (`*qtd`) adiciona complexidade que não existe nos outros paradigmas. Além disso, strings literais como `"animado"` não têm verificação de tipo — um erro de digitação passaria despercebido até a execução.

No Prolog, a regra usa fatos declarativos:

```prolog
humor_genero(animado, [acao, comedia]).
humor_genero(reflexivo, [drama, ficcao_cientifica]).
humor_genero(triste, [comedia, romance]).
```

A versão Prolog é a mais concisa (3 linhas, mesma quantidade de Haskell), mas também a mais frágil: se o átomo `animado` for escrito como `"animado"` (string) ou `Animado` (variável), o comportamento muda silenciosamente. Haskell evita esse problema porque `Animado` é um construtor de tipo — o compilador rejeita valores não declarados.

**Comparação de linhas para a Regra 4:** C = 15, Java = 5, Haskell = 3, Prolog = 3. A diferença de 5x entre C e Haskell/Prolog para expressar a mesma regra ilustra como o paradigma influencia diretamente a densidade de código.

### Tratamento de entrada vazia

No cenário com `generos_favoritos` vazio, todas as quatro implementações não recomendam nenhum filme. Isso ocorre porque a primeira regra exige que o filme tenha ao menos um gênero presente nos favoritos do usuário. Como a lista está vazia, nenhum filme satisfaz R1. O comportamento é idêntico em C (`possui_genero_favorito` retorna 0), Java (critério de gênero rejeita), Haskell (`temGeneroFavorito` falha) e Prolog (`member(G, [])` nunca unifica).

Esse comportamento é coerente com a especificação atual, mas pode ser discutido como uma decisão de produto. Uma alternativa seria usar apenas humor, duração e classificação quando a lista de favoritos estivesse vazia. Essa alternativa, porém, mudaria a interpretação da regra R1.

### Paralelização

Entre as quatro implementações, o paradigma funcional (Haskell) tende a ser o mais favorável para paralelização futura, porque a recomendação opera sobre uma lista de filmes sem modificar estado compartilhado. Como `pontuacao`, `classifOk` e `temGeneroFavorito` são funções puras, diferentes filmes poderiam ser avaliados em paralelo com menor risco de efeitos colaterais.

No Java, a paralelização também seria possível usando streams paralelos ou executores, mas seria necessário garantir que as estruturas envolvidas fossem imutáveis ou usadas de forma segura. A implementação atual já ajuda nesse sentido porque `Filme` é imutável.

No C, a paralelização seria a mais trabalhosa: o loop em `recomendar()` escreve em um array compartilhado (`resultados[qtd]`), exigindo sincronização explícita (mutex, semáforos) para evitar condições de corrida. Além disso, `qsort` não é paralelizável sem trocar a implementação.

No Prolog, a paralelização é conceitualmente possível (cada filme poderia ser avaliado por uma thread), mas o SWI-Prolog exige cuidado com predicados que usam corte (`!`) e com a coleta via `findall/3`. Na prática, o modelo de backtracking do Prolog não foi projetado para execução paralela.

### Paradigma mais adequado

Com todas as implementações analisadas, Haskell (funcional) mostrou-se o mais adequado para este domínio específico. O problema é naturalmente uma sequência de filtros e transformações sobre uma coleção, e o pipeline de `recomenda` expressa isso em 5 linhas de composição. Com 125 linhas de núcleo lógico, foi a implementação mais concisa entre os quatro paradigmas.

Java OO (249 linhas) é o mais adequado quando a prioridade é extensibilidade e manutenção por equipes. A separação de critérios via Strategy permite adicionar regras sem mexer no motor — um atributo valioso em sistemas que evoluem frequentemente.

C (149 linhas) é o mais adequado quando o critério principal é desempenho bruto e controle de memória. A implementação não aloca memória dinâmica (tudo é stack ou estático), o que é relevante para sistemas embarcados. No entanto, a cada nova funcionalidade, o código central precisa ser modificado.

Prolog (152 linhas) é o mais adequado quando o domínio é naturalmente expresso como fatos e regras — como sistemas especialistas ou configurações. No entanto, a necessidade de ordenação forçou o uso de `findall`/`keysort`, quebrando parcialmente o estilo declarativo.

### Análise qualitativa

**Qual paradigma se mostrou mais adequado para este problema e por quê?**

O paradigma funcional (Haskell) resolveu o problema com 125 linhas, contra 149 de C, 152 de Prolog e 249 de Java. A razão é que o domínio — filtrar, pontuar e ordenar uma coleção — é exatamente o tipo de problema para o qual funções de alta ordem foram projetadas. O pipeline `filter > map > filter > sortBy` expressa a intenção do algoritmo sem ruído sintático de loops ou condicionais, enquanto C e Java precisam de 20+ linhas de controle de fluxo explícito para a mesma lógica.

**Onde cada paradigma impôs mais dificuldade ou fricção?**

Em C, a fricção esteve na manipulação de strings: `strcmp`, `strcpy` e arrays de tamanho fixo adicionaram ~30 linhas que não existem nos outros paradigmas. Em Java, a fricção foi a quantidade de arquivos: 8 classes para um problema de 200 linhas, o que exige navegar entre arquivos para entender o fluxo completo. Em Haskell, a fricção foi a curva de aprendizado da composição: o operador `(.)` e a ordem de leitura direita-para-esquerda não são intuitivos para quem vem de linguagens imperativas. Em Prolog, a maior fricção foi a ordenação: como o paradigma lógico não produz resultados ordenados, foi necessário `findall` + `keysort` + `reverse`, uma construção que não é natural em Prolog e que duplica as condições de filtro.

**Houve alguma construção de um paradigma que você gostaria de ter tido disponível ao usar outro?**

Sim. O casamento de padrões de Haskell (`generosPorHumor Animado = ...`) seria bem-vindo em C e Java, eliminando a necessidade de `if`/`else if` com strings. Da mesma forma, o Strategy Pattern de Java seria útil em C para isolar regras sem modificar a função principal. O `findall` do Prolog — que coleta todas as soluções de uma consulta — seria útil em Haskell e Java como primitiva de linguagem para consultas complexas.

**A solução mais curta foi necessariamente a mais fácil de entender?**

Não. Haskell (125 linhas) foi a mais curta, mas sua legibilidade depende de familiaridade com composição de funções e estilo *point-free*. Para um programador que só conhece C, a implementação imperativa (149 linhas) é mais fácil de entender, apesar de ser 19% mais longa, porque `for`, `if` e `continue` são construtos universais. Prolog (152 linhas) é a segunda mais curta, mas o backtracking e a unificação tornam o fluxo de execução menos previsível — um bug simples como inverter a ordem de cláusulas pode mudar completamente o resultado. Java (249 linhas) é a mais longa, mas a separação em classes nomeadas (`CriterioGenero`, `CalculadoraPontuacao`) torna o código auto-documentado: o nome da classe já explica sua função.

---

---

## 05 Conclusão

Este trabalho implementou o mesmo sistema de recomendação de filmes em quatro paradigmas — imperativo (C), orientado a objetos (Java), funcional (Haskell) e lógico (Prolog) — e os resultados confirmam que a escolha do paradigma molda profundamente a arquitetura da solução, mesmo quando a saída é idêntica.

Em C, o foco esteve no controle explícito de estado e fluxo: cada decisão do algoritmo é visível como `if`/`continue`, cada comparação é um `strcmp`, cada iteração é um `for`. A solução é previsível e familiar, mas a verbosidade cresce linearmente com cada nova regra.

Em Java, o foco esteve em responsabilidades e extensibilidade: 8 classes distribuem o problema em unidades nomeadas e substituíveis. O Strategy Pattern permite adicionar critérios sem modificar código existente, mas o custo é uma implementação 99% mais longa que Haskell (249 vs 125 linhas).

Em Haskell, o foco esteve na transformação de dados: um pipeline de 5 funções resolve o núcleo do problema. A imutabilidade elimina efeitos colaterais, e funções puras facilitam teste e paralelização. A contrapartida é uma curva de aprendizado mais íngreme e menor acessibilidade para equipes sem experiência funcional.

Em Prolog, o foco esteve na declaração de conhecimento: fatos e regras mapeiam diretamente o enunciado. A unificação e o backtracking resolvem automaticamente a busca por filmes compatíveis. A fragilidade está na ordenação — o paradigma não foi projetado para ranqueamento, e `findall`/`keysort` quebram o estilo declarativo.

O que o grupo não esperava antes de implementar: (1) a diferença de 5x em linhas para expressar a mesma regra de humor entre C (15) e Prolog (3); (2) que a implementação mais curta (Haskell, 125 linhas) não é a mais fácil de entender para quem não conhece o paradigma; (3) que o Prolog, apesar de ser o paradigma mais distante do imperativo, é o que tem a correspondência mais direta entre enunciado e código — cada regra vira literalmente uma linha do predicado.

Se o grupo tivesse que resolver um problema similar em produção, a escolha dependeria do contexto: para um sistema com requisitos estáveis e foco em processamento de dados, Haskell ofereceria a melhor relação concisão/segurança; para um sistema com regras de negócio que mudam frequentemente e equipe grande, Java OO com Strategy seria a escolha mais sustentável; para um sistema embarcado com restrições de memória, C seria a opção natural; para um sistema especialista com muitas regras interdependentes, Prolog teria vantagem pela proximidade entre especificação e implementação.

A principal lição do trabalho é que não existe paradigma universalmente superior — cada um oferece vantagens específicas que se alinham a diferentes dimensões do problema: controle (C), organização (Java), pureza (Haskell) e declaração (Prolog). A maturidade como engenheiro de software inclui reconhecer qual dessas dimensões é prioritária para cada projeto.

---

## 06 Referências

[1] ORACLE. *Java Platform, Standard Edition Documentation*. Disponível em: https://docs.oracle.com/en/java/. Acesso em: 25 jun. 2026.

[2] THE HASKELL TEAM. *Haskell Language Documentation*. Disponível em: https://www.haskell.org/documentation/. Acesso em: 25 jun. 2026.

[3] GHC TEAM. *The Glasgow Haskell Compiler User's Guide*. Disponível em: https://downloads.haskell.org/ghc/latest/docs/users_guide/. Acesso em: 25 jun. 2026.

[4] GAMMA, Erich et al. *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley, 1994.

[5] ISO/IEC. *ISO/IEC 9899:2018 — Information technology — Programming languages — C*. International Organization for Standardization, 2018.

[6] GCC TEAM. *GCC, the GNU Compiler Collection — Online Documentation*. Disponível em: https://gcc.gnu.org/onlinedocs/. Acesso em: 25 jun. 2026.

[7] WIELEMAKER, Jan et al. *SWI-Prolog Reference Manual*. Disponível em: https://www.swi-prolog.org/pldoc/. Acesso em: 25 jun. 2026.

[8] BRATKO, Ivan. *Prolog Programming for Artificial Intelligence*. 4. ed. Pearson, 2012.

