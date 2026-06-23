package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

public class CriterioClassificacao implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfilUsuario) {
        return perfilUsuario.getClassificacaoEtaria().permiteAssistir(filme.getClassificacaoEtaria());
    }
}
