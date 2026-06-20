package recomendacao.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class PerfilUsuario {
    private final Set<Genero> generosFavoritos;
    private final Humor humor;
    private final int duracaoMaxima;
    private final Classificacao classificacao;

    public PerfilUsuario(Set<Genero> generosFavoritos, Humor humor, int duracaoMaxima, Classificacao classificacao) {
        if (generosFavoritos == null || generosFavoritos.isEmpty()) {
            throw new IllegalArgumentException("Perfil deve possuir pelo menos um genero favorito.");
        }

        if (humor == null) {
            throw new IllegalArgumentException("Humor deve ser informado.");
        }

        if (duracaoMaxima <= 0) {
            throw new IllegalArgumentException("Duracao maxima deve ser positiva.");
        }

        if (classificacao == null) {
            throw new IllegalArgumentException("Classificacao do perfil deve ser informada.");
        }

        this.generosFavoritos = Collections.unmodifiableSet(EnumSet.copyOf(generosFavoritos));
        this.humor = humor;
        this.duracaoMaxima = duracaoMaxima;
        this.classificacao = classificacao;
    }

    public Set<Genero> getGenerosFavoritos() {
        return generosFavoritos;
    }

    public Humor getHumor() {
        return humor;
    }

    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }
}
