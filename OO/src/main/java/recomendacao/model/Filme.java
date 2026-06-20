package recomendacao.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Filme {
    private final String titulo;
    private final List<Genero> generos;
    private final int duracaoMinutos;
    private final Classificacao classificacao;

    public Filme(String titulo, List<Genero> generos, int duracaoMinutos, Classificacao classificacao) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Titulo do filme deve ser informado.");
        }

        if (generos == null || generos.isEmpty()) {
            throw new IllegalArgumentException("Filme deve possuir pelo menos um genero.");
        }

        if (duracaoMinutos <= 0) {
            throw new IllegalArgumentException("Duracao do filme deve ser positiva.");
        }

        if (classificacao == null) {
            throw new IllegalArgumentException("Classificacao do filme deve ser informada.");
        }

        this.titulo = titulo;
        this.generos = Collections.unmodifiableList(new ArrayList<>(generos));
        this.duracaoMinutos = duracaoMinutos;
        this.classificacao = classificacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }
}
