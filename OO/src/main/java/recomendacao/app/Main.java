package recomendacao.app;

import recomendacao.criterio.CriterioClassificacao;
import recomendacao.criterio.CriterioDuracao;
import recomendacao.criterio.CriterioGenero;
import recomendacao.criterio.CriterioRecomendacao;
import recomendacao.model.ClassificacaoEtaria;
import recomendacao.model.Filme;
import recomendacao.model.FilmeRecomendado;
import recomendacao.model.PerfilUsuario;
import recomendacao.repository.CatalogoFilmes;
import recomendacao.service.CalculadoraPontuacao;
import recomendacao.service.MotorRecomendacao;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CatalogoFilmes catalogoFilmes = new CatalogoFilmes();
        List<Filme> catalogo = catalogoFilmes.listarFilmes();

        List<CriterioRecomendacao> criterios = List.of(
                new CriterioGenero(),
                new CriterioDuracao(),
                new CriterioClassificacao()
        );

        CalculadoraPontuacao calculadoraPontuacao = new CalculadoraPontuacao();
        MotorRecomendacao motorRecomendacao = new MotorRecomendacao(criterios, calculadoraPontuacao);

<<<<<<< HEAD
        imprimirRecomendacoes(
                "Saida esperada",
                motorRecomendacao.recomendar(catalogo, perfilUsuario)
        );
=======
        List<CenarioEntrada> cenarios = List.of(
                new CenarioEntrada(
                        "Caso obrigatorio do enunciado",
                        new PerfilUsuario(
                                List.of("acao", "ficcao_cientifica"),
                                "animado",
                                150,
                                ClassificacaoEtaria.CATORZE
                        )
                ),
                new CenarioEntrada(
                        "Perfil reflexivo amplo",
                        new PerfilUsuario(
                                List.of("drama", "romance"),
                                "reflexivo",
                                180,
                                ClassificacaoEtaria.DEZOITO
                        )
                ),
                new CenarioEntrada(
                        "Perfil sem generos favoritos",
                        new PerfilUsuario(
                                List.of(),
                                "triste",
                                140,
                                ClassificacaoEtaria.LIVRE
                        )
                )
        );

        for (CenarioEntrada cenario : cenarios) {
            imprimirRecomendacoes(
                    cenario.titulo(),
                    motorRecomendacao.recomendar(catalogo, cenario.perfilUsuario())
            );
        }
>>>>>>> ce3ffa35de210c3aa4a5da632ea7cd0742b91e56
    }

    private static void imprimirRecomendacoes(String titulo, List<FilmeRecomendado> recomendacoes) {
        System.out.println(titulo);
        System.out.println("-".repeat(titulo.length()));

        if (recomendacoes.isEmpty()) {
            System.out.println("Nenhum filme recomendado.");
            System.out.println();
            return;
        }

        for (FilmeRecomendado recomendacao : recomendacoes) {
            Filme filme = recomendacao.getFilme();
            System.out.printf(
                    "[%d pts] %s (%s, %d min, %s)%n",
                    recomendacao.getPontuacao(),
                    filme.getTitulo(),
                    String.join(" + ", filme.getGeneros()),
                    filme.getDuracaoMinutos(),
                    filme.getClassificacaoEtaria()
            );
        }

        System.out.println();
    }

    private record CenarioEntrada(String titulo, PerfilUsuario perfilUsuario) {
    }
}
