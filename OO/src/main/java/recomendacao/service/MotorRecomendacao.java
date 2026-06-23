package recomendacao.service;

import recomendacao.criterio.CriterioRecomendacao;
import recomendacao.model.Filme;
import recomendacao.model.FilmeRecomendado;
import recomendacao.model.PerfilUsuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MotorRecomendacao {
    private static final int PONTUACAO_MINIMA = 2;

    private final List<CriterioRecomendacao> criterios;
    private final CalculadoraPontuacao calculadoraPontuacao;

    public MotorRecomendacao(List<CriterioRecomendacao> criterios, CalculadoraPontuacao calculadoraPontuacao) {
        this.criterios = new ArrayList<>(criterios);
        this.calculadoraPontuacao = calculadoraPontuacao;
    }

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

    private boolean todosCriteriosAceitam(Filme filme, PerfilUsuario perfilUsuario) {
        for (CriterioRecomendacao criterio : criterios) {
            if (!criterio.aceita(filme, perfilUsuario)) {
                return false;
            }
        }

        return true;
    }

    private void adicionarSeTiverPontuacaoMinima(
            List<FilmeRecomendado> recomendacoes,
            Filme filme,
            PerfilUsuario perfilUsuario
    ) {
        int pontuacao = calculadoraPontuacao.calcular(filme, perfilUsuario);

        if (pontuacao >= PONTUACAO_MINIMA) {
            recomendacoes.add(new FilmeRecomendado(filme, pontuacao));
        }
    }
}
