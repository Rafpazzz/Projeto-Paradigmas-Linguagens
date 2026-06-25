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
4. As implementações C, Java e Haskell imprimem a saída no mesmo formato; Prolog retorna pares `Pontuação-Título` na consulta.

---

## 03 Implementações por paradigma

| Paradigma | Linguagem utilizada | Linhas de código atuais | Responsável |
|---|---:|---:|---|
| Imperativo | C / GCC 13.2.0 | 178 linhas de núcleo lógico | Crisfield |
| Orientado a objetos | Java 21 | 212 linhas de núcleo lógico | Rafael |
| Funcional | Haskell / GHC 9.10.3 | 141 linhas de núcleo lógico | João |
| Lógico | Prolog / SWI-Prolog 9.2+ | 135 linhas de núcleo lógico | Crisfield |

> As linhas foram medidas excluindo linhas vazias e comentários. Para C, excluiu-se a função `main`; para Java, excluíram-se imports, declaração de pacote e a classe de I/O `Main`; para Haskell e Prolog, contou-se o arquivo completo por ele concentrar a lógica do problema.

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
        if(!classif_ok(filme.classificacao,
                       usuario.classificacao))
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
    int i, j;

    for(i = 0; i < filme.qtd_generos; i++) {
        for(j = 0; j < usuario.qtd_favoritos; j++) {
            if(strcmp(filme.generos[i],
                      usuario.generos_favoritos[j]) == 0) {
                pontos++;
            }
        }
    }

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

A função de pontuação em C segue a seção 2.4 do enunciado: soma `+1` para cada gênero favorito encontrado e `+1` quando há coincidência com os gêneros priorizados pelo humor. A diferença em relação aos outros paradigmas está na quantidade de controle manual: são necessários loops aninhados, `strcmp`, arrays de prioridade e `strcpy` para expressar a mesma regra.

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

Cada regra de recomendação é representada por predicados ou chamadas declarativas. A regra R1 é verificada diretamente com duas chamadas a `member/2`, uma sobre os gêneros do filme e outra sobre os favoritos do usuário. `classif_ok/2` resolve R3 por comparação numérica. `bonus_humor/3` implementa R4 com um corte (`!`) para evitar múltiplas soluções. A pontuação é calculada por `pontuacao/4`, que compõe `contar_generos/3` (recursivo) com o bônus de humor, seguindo a mesma fórmula usada nas demais implementações.

O predicado principal `recomenda/5` unifica todas as regras em uma cláusula: o filme deve ter gênero favorito (R1), duração compatível (R2), classificação compatível (R3) e pontuação mínima 2 (R5). Para ordenar os resultados, `recomenda_filmes/5` coleta pares `P-Titulo` com `findall/3`, ordena com `keysort/2` e inverte com `reverse/2` para obter pontuação decrescente.

A elegância do Prolog está na correspondência direta entre a especificação e o código: cada regra do enunciado vira literalmente uma condição na cláusula `recomenda/5`. A principal dificuldade é que a ordenação não é natural no paradigma lógico puro — o backtracking gera soluções em ordem arbitrária, exigindo `findall` + `keysort` + `reverse` como etapa extra.

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

Como o Prolog não ordena resultados nativamente, `recomenda_filmes/5` coleta todas as soluções com `findall/3`, ordena por pontuação com `keysort/2` e inverte a lista com `reverse/2` para ordem decrescente. Essa é a parte do código que mais se afasta do estilo puramente declarativo.

**Cálculo da pontuação em Prolog**

```prolog
pontuacao(Generos, Favoritos, Humor, Pontos) :-
    contar_generos(Generos, Favoritos, P1),
    bonus_humor(Generos, Humor, P2),
    Pontos is P1 + P2.
```

A função `contar_generos/3` usa recursão para somar coincidências de gênero. `bonus_humor/3` usa `member/2` para verificar interseção com os gêneros priorizados pelo humor e emprega corte (`!`) para evitar soluções múltiplas quando o bônus já foi concedido.

---

## 04 Análise comparativa

### Tabela comparativa

| Critério | Imperativo | Orientado a objetos | Funcional | Lógico |
|---|---|---|---|---|
| Linhas de código | 178 linhas de núcleo lógico | 212 linhas de núcleo lógico | 141 linhas de núcleo lógico | 135 linhas de núcleo lógico |
| Legibilidade | O fluxo é linear, mas depende de `for`, `if` e `continue`, com estado explícito espalhado no algoritmo. | A lógica fica separada em classes, o que melhora a organização, mas aumenta o número de arquivos para acompanhar o fluxo. | A leitura é concentrada em poucas funções compostas, sem estado mutável. | As regras são próximas do enunciado, mas a ordenação exige predicados auxiliares e sai do estilo declarativo puro. |
| Facilidade de depuração | É direta em depurador, porque o estado é visível passo a passo. | É modular, porque cada classe pode ser isolada. | É boa em funções puras, mas o pipeline completo exige acompanhar composição. | É mais difícil por causa de backtracking e da ordem das cláusulas. |
| Adequação ao domínio | O domínio cabe bem em filtros e ordenação, mas exige manipulação manual de strings e arrays fixos. | O domínio encaixa bem em entidades e regras separadas. | O domínio encaixa bem em transformação de listas. | O núcleo encaixa bem em fatos e regras, mas a ordenação precisa de contorno extra. |
| Curva de aprendizado | Baixa, porque usa estruturas conhecidas. | Moderada, porque exige entender classes, interfaces e delegação. | Alta, porque exige familiaridade com composição e imutabilidade. | Alta, porque exige unificação, backtracking e outra forma de pensar. |
| Facilidade de extensão | Média, porque cada nova regra entra no fluxo principal. | Alta, porque novos critérios podem virar classes novas. | Boa, porque novos filtros entram no pipeline com pouca mudança estrutural. | Média, porque novas regras são naturais, mas ordenação e pontuação podem exigir revisão de mais de um predicado. |

### Análise qualitativa

**Qual paradigma se mostrou mais adequado para este problema e por quê?**

Haskell foi o mais adequado porque o problema se reduz a uma sequência de filtros, cálculo de pontuação e ordenação sobre uma lista. Pela contagem atual, ficou com 141 linhas de núcleo lógico, abaixo de C (178) e Java (212), e próximo de Prolog (135). A vantagem técnica está na forma do código: a implementação funcional concentra a lógica em um pipeline de funções puras, o que reduz a quantidade de estado manual necessário para expressar o algoritmo.

A diferença não é só de volume. Em Java, a mesma solução ficou espalhada em classes e arquivos porque cada responsabilidade foi separada em uma unidade própria. Em C, a lógica continuou explícita em loops, condicionais, comparação manual de strings e funções extras para formatar a saída igual a Java/Haskell. Prolog foi o menor em linhas, mas exigiu `findall`, `keysort` e `reverse` para ranquear os resultados. Haskell foi o mais aderente ao formato do problema porque expressa diretamente transformação de dados.

**Onde cada paradigma impôs mais dificuldade ou fricção?**

Em C, a fricção está no estado manual e nas strings. O algoritmo depende de `for`, `if`, `continue`, `strcmp` e arrays fixos, então parte relevante do código é dedicada a controlar detalhes de implementação em vez de regras de negócio. Isso aumenta a superfície de erro e deixa a manutenção mais sensível a mudanças pequenas, como alterar uma condição ou ajustar o catálogo.

Em Java, a fricção está na dispersão do fluxo entre várias classes, o que obriga a navegar por mais arquivos para entender o comportamento completo. Em Haskell, a fricção está na composição e na imutabilidade, que são concisas no código mas exigem familiaridade com o estilo funcional. Em Prolog, a fricção está no backtracking e na ordenação, porque a solução precisou sair do núcleo declarativo e recorrer a predicados auxiliares para ranquear resultados.

**Houve alguma construção de um paradigma que você gostaria de ter tido disponível ao usar outro?**

Sim. O padrão Strategy de Java seria útil em C para isolar regras sem alterar o núcleo principal, porque no código em C cada nova regra entra como mais um `if` ou `continue` dentro da função central. Isso tornaria a extensão menos acoplada ao fluxo principal e reduziria o crescimento da função `recomendar`.

Em Haskell, um mecanismo mais direto de colecionar resultados como o `findall` do Prolog facilitaria consultas mais complexas, principalmente quando o problema deixa de ser apenas um pipeline linear. O casamento de padrões de Haskell também seria útil em C e Java para reduzir `if`/`else if` com strings, porque transformaria escolhas condicionais em expressões mais explícitas e menos sujeitas a erro.

**A solução mais curta foi necessariamente a mais fácil de entender?**

Não. A implementação mais curta pela contagem atual foi Prolog, com 135 linhas de núcleo lógico, mas ela exige familiaridade com unificação, backtracking, `findall`, `keysort` e `reverse`. A redução de linhas não elimina a complexidade de entender como as soluções são geradas e depois ordenadas.

C e Java são mais fáceis de ler para quem já conhece paradigmas imperativos, mesmo com mais código, porque `for`, `if`, chamadas de método e classes nomeadas são estruturas familiares. Haskell ficou menor que C após a padronização da saída, mas sua leitura depende de entender composição de funções e tipos algébricos. Portanto, a menor quantidade de linhas não significa automaticamente menor dificuldade.

---

## 05 Conclusão

Este trabalho implementou o mesmo sistema de recomendação de filmes em quatro paradigmas — imperativo (C), orientado a objetos (Java), funcional (Haskell) e lógico (Prolog) — e os resultados confirmam que a escolha do paradigma molda profundamente a arquitetura da solução, mesmo quando as recomendações calculadas são equivalentes.

Em C, o foco esteve no controle explícito de estado e fluxo: cada decisão do algoritmo é visível como `if`/`continue`, cada comparação é um `strcmp`, cada iteração é um `for`. A solução é previsível e familiar, mas a verbosidade cresce linearmente com cada nova regra.

Em Java, o foco esteve em responsabilidades e extensibilidade: as classes distribuem o problema em unidades nomeadas e substituíveis. O Strategy Pattern permite adicionar critérios sem modificar o motor principal, mas o custo é uma implementação maior que as demais na contagem de núcleo lógico (212 linhas).

Em Haskell, o foco esteve na transformação de dados: um pipeline de 5 funções resolve o núcleo do problema. A imutabilidade elimina efeitos colaterais, e funções puras facilitam teste e paralelização. A contrapartida é uma curva de aprendizado mais íngreme e menor acessibilidade para equipes sem experiência funcional.

Em Prolog, o foco esteve na declaração de conhecimento: fatos e regras mapeiam diretamente o enunciado. A unificação e o backtracking resolvem automaticamente a busca por filmes compatíveis. A fragilidade está na ordenação — o paradigma não foi projetado para ranqueamento, e `findall`/`keysort`/`reverse` quebram o estilo declarativo.

O que o grupo não esperava antes de implementar: (1) a diferença de 5x em linhas para expressar a mesma regra de humor entre C (15) e Prolog (3); (2) que a implementação mais curta em linhas não é necessariamente a mais fácil de entender; (3) que o Prolog, apesar de ser o paradigma mais distante do imperativo, é o que tem a correspondência mais direta entre enunciado e código na parte de fatos e regras.

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
