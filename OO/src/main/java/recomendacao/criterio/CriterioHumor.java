package recomendacao.criterio;

import recomendacao.model.Filme;
import recomendacao.model.Genero;
import recomendacao.model.PerfilUsuario;

public class CriterioHumor implements CriterioRecomendacao {
    @Override
    public boolean aceita(Filme filme, PerfilUsuario perfil) {
        return true;
    }

    @Override
    public int calcularPontuacao(Filme filme, PerfilUsuario perfil) {
        for (Genero genero : filme.getGeneros()) {
            if (perfil.getHumor().getGenerosPriorizados().contains(genero)) {
                return 1;
            }
        }

        return 0;
    }
}
