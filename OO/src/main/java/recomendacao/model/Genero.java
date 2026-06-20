package recomendacao.model;

import java.text.Normalizer;

public enum Genero {
    ACAO("ação"),
    COMEDIA("comédia"),
    DRAMA("drama"),
    FICCAO_CIENTIFICA("ficção científica"),
    ROMANCE("romance");

    private final String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Genero fromDescricao(String descricao) {
        String valorNormalizado = normalizar(descricao);

        for (Genero genero : values()) {
            if (normalizar(genero.descricao).equals(valorNormalizado)) {
                return genero;
            }
        }

        throw new IllegalArgumentException("Genero invalido: " + descricao);
    }

    private static String normalizar(String texto) {
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
