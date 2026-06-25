package recomendacao.service;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

import java.util.List;
import java.util.Map;

public class CalculadoraPontuacao {
    private static final Map<String, List<String>> GENEROS_PRIORIZADOS_POR_HUMOR = Map.of(
            "animado", List.of("acao", "comedia"),
            "reflexivo", List.of("drama", "ficcao_cientifica"),
            "triste", List.of("comedia", "romance")
    );

    public int calcular(Filme filme, PerfilUsuario perfilUsuario) {
        int pontosPorCriterioGenero = calcularPontosPorCriterioGenero(filme, perfilUsuario);
        int pontosPorGenerosFavoritos = calcularPontosPorGenerosFavoritos(filme, perfilUsuario);
        int pontosPorHumor = calcularPontosPorHumor(filme, perfilUsuario);

        return pontosPorCriterioGenero + pontosPorGenerosFavoritos + pontosPorHumor;
    }

    private int calcularPontosPorCriterioGenero(Filme filme, PerfilUsuario perfilUsuario) {
        if (filme.possuiAlgumGenero(perfilUsuario.getGenerosFavoritos())) {
            return 1;
        }

        return 0;
    }

    private int calcularPontosPorGenerosFavoritos(Filme filme, PerfilUsuario perfilUsuario) {
        int total = 0;

        for (String genero : filme.getGeneros()) {
            if (perfilUsuario.getGenerosFavoritos().contains(genero)) {
                total++;
            }
        }

        return total;
    }

    private int calcularPontosPorHumor(Filme filme, PerfilUsuario perfilUsuario) {
        if (filme.possuiAlgumGenero(generosPriorizados(perfilUsuario))) {
            return 1;
        }

        return 0;
    }

    private List<String> generosPriorizados(PerfilUsuario perfilUsuario) {
        return GENEROS_PRIORIZADOS_POR_HUMOR.getOrDefault(perfilUsuario.getHumor(), List.of());
    }
}
