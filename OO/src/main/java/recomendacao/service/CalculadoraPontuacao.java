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
        int generosFavoritosEncontrados = contarGenerosFavoritos(filme, perfilUsuario);
        int pontuacao = generosFavoritosEncontrados > 0 ? 1 : 0;

        pontuacao += generosFavoritosEncontrados;

        if (filme.possuiAlgumGenero(generosPriorizados(perfilUsuario))) {
            pontuacao++;
        }

        return pontuacao;
    }

    private int contarGenerosFavoritos(Filme filme, PerfilUsuario perfilUsuario) {
        int total = 0;

        for (String genero : filme.getGeneros()) {
            if (perfilUsuario.getGenerosFavoritos().contains(genero)) {
                total++;
            }
        }

        return total;
    }

    private List<String> generosPriorizados(PerfilUsuario perfilUsuario) {
        return GENEROS_PRIORIZADOS_POR_HUMOR.getOrDefault(perfilUsuario.getHumor(), List.of());
    }
}
