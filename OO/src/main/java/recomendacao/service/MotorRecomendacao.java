package recomendacao.service;

import recomendacao.criterio.CriterioRecomendacao;
import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;
import recomendacao.model.Recomendacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MotorRecomendacao {
    private static final int PONTUACAO_MINIMA = 2;

    private final List<CriterioRecomendacao> criterios;

    public MotorRecomendacao(List<CriterioRecomendacao> criterios) {
        if (criterios == null || criterios.isEmpty()) {
            throw new IllegalArgumentException("Ao menos um criterio deve ser informado.");
        }

        this.criterios = List.copyOf(criterios);
    }

    public List<Recomendacao> recomendar(List<Filme> filmes, PerfilUsuario perfil) {
        List<Recomendacao> recomendacoes = new ArrayList<>();

        for (Filme filme : filmes) {
            if (!aceitoPorTodosOsCriterios(filme, perfil)) {
                continue;
            }

            int pontuacao = calcularPontuacaoTotal(filme, perfil);

            if (pontuacao >= PONTUACAO_MINIMA) {
                recomendacoes.add(new Recomendacao(filme, pontuacao));
            }
        }

        recomendacoes.sort(
                Comparator.comparingInt(Recomendacao::getPontuacao)
                        .reversed()
                        .thenComparing(recomendacao -> recomendacao.getFilme().getTitulo())
        );

        return recomendacoes;
    }

    private boolean aceitoPorTodosOsCriterios(Filme filme, PerfilUsuario perfil) {
        for (CriterioRecomendacao criterio : criterios) {
            if (!criterio.aceita(filme, perfil)) {
                return false;
            }
        }

        return true;
    }

    private int calcularPontuacaoTotal(Filme filme, PerfilUsuario perfil) {
        int pontuacao = 0;

        for (CriterioRecomendacao criterio : criterios) {
            pontuacao += criterio.calcularPontuacao(filme, perfil);
        }

        return pontuacao;
    }
}
