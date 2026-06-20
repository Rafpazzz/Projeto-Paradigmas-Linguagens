package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

public class CriterioClassificacao implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfil) {
        return perfil.getClassificacao().permite(filme.getClassificacao());
    }

    @Override
    public int calcularPontuacao(Filme filme, PerfilUsuario perfil) {
        return 0;
    }
}
