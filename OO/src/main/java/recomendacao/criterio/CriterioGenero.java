package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.Genero;
import recomendacao.model.PerfilUsuario;

public class CriterioGenero implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfil) {
        return calcularPontuacao(filme, perfil) > 0;
    }

    @Override
    public int calcularPontuacao(Filme filme, PerfilUsuario perfil) {
        int pontuacao = 0;

        for (Genero genero : filme.getGeneros()) {
            if (perfil.getGenerosFavoritos().contains(genero)) {
                pontuacao++;
            }
        }

        return pontuacao;
    }
}
