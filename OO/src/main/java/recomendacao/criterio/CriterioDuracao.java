package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

public class CriterioDuracao implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfil) {
        return filme.getDuracaoMinutos() <= perfil.getDuracaoMaxima();
    }

    @Override
    public int calcularPontuacao(Filme filme, PerfilUsuario perfil) {
        return aceita(filme, perfil) ? 1 : 0;
    }
}
