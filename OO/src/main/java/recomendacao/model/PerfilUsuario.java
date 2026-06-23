package recomendacao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerfilUsuario {
    private List<String> generosFavoritos;
    private String humor;
    private int duracaoMaxima;
    private ClassificacaoEtaria classificacaoEtaria;

    public PerfilUsuario(
            List<String> generosFavoritos,
            String humor,
            int duracaoMaxima,
            ClassificacaoEtaria classificacaoEtaria
    ) {
        atualizarPerfil(generosFavoritos, humor, duracaoMaxima, classificacaoEtaria);
    }

    public void atualizarPerfil(
            List<String> novosGenerosFavoritos,
            String novoHumor,
            int novaDuracaoMaxima,
            ClassificacaoEtaria novaClassificacaoEtaria
    ) {
        this.generosFavoritos = new ArrayList<>(novosGenerosFavoritos);
        this.humor = novoHumor;
        this.duracaoMaxima = novaDuracaoMaxima;
        this.classificacaoEtaria = novaClassificacaoEtaria;
    }

    public List<String> getGenerosFavoritos() {
        return Collections.unmodifiableList(generosFavoritos);
    }

    public String getHumor() {
        return humor;
    }

    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public ClassificacaoEtaria getClassificacaoEtaria() {
        return classificacaoEtaria;
    }
}
