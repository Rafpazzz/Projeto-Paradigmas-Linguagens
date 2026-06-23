package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.PerfilUsuario;

public interface CriterioRecomendacao {
    boolean aceita(Filme filme, PerfilUsuario perfilUsuario);
}
