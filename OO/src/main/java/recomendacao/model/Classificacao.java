package recomendacao.model;

public enum Classificacao {
    LIVRE(0, "livre"),
    DOZE(12, "12"),
    QUATORZE(14, "14"),
    DEZOITO(18, "18");

    private final int idadeMinima;
    private final String descricao;

    Classificacao(int idadeMinima, String descricao) {
        this.idadeMinima = idadeMinima;
        this.descricao = descricao;
    }

    public boolean permite(Classificacao classificacaoFilme) {
        return idadeMinima >= classificacaoFilme.idadeMinima;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Classificacao fromDescricao(String descricao) {
        String valor = descricao.trim().toLowerCase();

        for (Classificacao classificacao : values()) {
            if (classificacao.descricao.equals(valor)) {
                return classificacao;
            }
        }

        throw new IllegalArgumentException("Classificacao invalida: " + descricao);
    }
}
