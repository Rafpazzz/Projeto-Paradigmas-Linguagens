package recomendacao.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Humor {
    ANIMADO(EnumSet.of(Genero.ACAO, Genero.COMEDIA)),
    REFLEXIVO(EnumSet.of(Genero.DRAMA, Genero.FICCAO_CIENTIFICA)),
    TRISTE(EnumSet.of(Genero.COMEDIA, Genero.ROMANCE));

    private final Set<Genero> generosPriorizados;

    Humor(Set<Genero> generosPriorizados) {
        this.generosPriorizados = Collections.unmodifiableSet(generosPriorizados);
    }

    public Set<Genero> getGenerosPriorizados() {
        return generosPriorizados;
    }
}
