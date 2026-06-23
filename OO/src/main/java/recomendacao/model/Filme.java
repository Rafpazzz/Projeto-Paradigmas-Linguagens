package recomendacao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filme {
    private final String titulo;
    private final List<String> generos;
    private final int duracaoMinutos;
    private final ClassificacaoEtaria classificacaoEtaria;

    public Filme(String titulo, List<String> generos, int duracaoMinutos, ClassificacaoEtaria classificacaoEtaria) {
        this.titulo = titulo;
        this.generos = new ArrayList<>(generos);
        this.duracaoMinutos = duracaoMinutos;
        this.classificacaoEtaria = classificacaoEtaria;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getGeneros() {
        return Collections.unmodifiableList(generos);
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public ClassificacaoEtaria getClassificacaoEtaria() {
        return classificacaoEtaria;
    }

    public boolean possuiAlgumGenero(List<String> generosBuscados) {
        for (String genero : generos) {
            if (generosBuscados.contains(genero)) {
                return true;
            }
        }

        return false;
    }
}
