package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

public class CriterioDuracao implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfilUsuario) {
        return filme.getDuracaoMinutos() <= perfilUsuario.getDuracaoMaxima();
    }
}
