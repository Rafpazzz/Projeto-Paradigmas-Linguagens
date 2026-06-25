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

        PerfilUsuario perfilUsuario = new PerfilUsuario(
                List.of("acao", "ficcao_cientifica"),
                "animado",
                150,
                ClassificacaoEtaria.CATORZE
        );

        List<CriterioRecomendacao> criterios = List.of(
                new CriterioGenero(),
                new CriterioDuracao(),
                new CriterioClassificacao()
        );

        CalculadoraPontuacao calculadoraPontuacao = new CalculadoraPontuacao();
        MotorRecomendacao motorRecomendacao = new MotorRecomendacao(criterios, calculadoraPontuacao);

        imprimirRecomendacoes(
                "Saida esperada",
                motorRecomendacao.recomendar(catalogo, perfilUsuario)
        );
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
}
