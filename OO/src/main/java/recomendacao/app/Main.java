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

        List<CenarioEntrada> cenarios = List.of(
                new CenarioEntrada(
                        "USUARIO 01",
                        new PerfilUsuario(
                                List.of("acao", "ficcao_cientifica"),
                                "animado",
                                150,
                                ClassificacaoEtaria.CATORZE
                        )
                ),
                new CenarioEntrada(
                        "USUARIO 02",
                        new PerfilUsuario(
                                List.of("drama", "romance"),
                                "reflexivo",
                                180,
                                ClassificacaoEtaria.DEZOITO
                        )
                ),
                new CenarioEntrada(
                        "USUARIO 03",
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
                    cenario,
                    motorRecomendacao.recomendar(catalogo, cenario.perfilUsuario())
            );
        }
    }

    private static void imprimirRecomendacoes(CenarioEntrada cenario, List<FilmeRecomendado> recomendacoes) {
        String cabecalho = cenario.titulo() + " (" + formatarPerfil(cenario.perfilUsuario()) + ")";

        System.out.println(cabecalho);
        System.out.println("-".repeat(cabecalho.length()));

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
                    String.join(", ", filme.getGeneros()),
                    filme.getDuracaoMinutos(),
                    filme.getClassificacaoEtaria()
            );
        }

        System.out.println();
    }

    private static String formatarPerfil(PerfilUsuario perfilUsuario) {
        String generos = perfilUsuario.getGenerosFavoritos().isEmpty()
                ? "nenhum"
                : String.join(", ", perfilUsuario.getGenerosFavoritos());

        return "generos: " + generos
                + " | humor: " + formatarHumor(perfilUsuario.getHumor())
                + " | duracao max: " + perfilUsuario.getDuracaoMaxima() + " min"
                + " | classificacao: " + perfilUsuario.getClassificacaoEtaria();
    }

    private static String formatarHumor(String humor) {
        return switch (humor) {
            case "animado" -> "Animado";
            case "reflexivo" -> "Reflexivo";
            case "triste" -> "Triste";
            default -> humor;
        };
    }

    private record CenarioEntrada(String titulo, PerfilUsuario perfilUsuario) {
    }
}
