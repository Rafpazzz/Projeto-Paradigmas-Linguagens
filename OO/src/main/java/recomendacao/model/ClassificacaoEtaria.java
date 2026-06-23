package recomendacao.model;

public enum ClassificacaoEtaria {
    LIVRE(0),
    DOZE(12),
    CATORZE(14),
    DEZOITO(18);

    private final int nivel;

    ClassificacaoEtaria(int nivel) {
        this.nivel = nivel;
    }

    public boolean permiteAssistir(ClassificacaoEtaria classificacaoDoFilme) {
        return classificacaoDoFilme.nivel <= nivel;
    }

    @Override
    public String toString() {
        if (this == LIVRE) {
            return "livre";
        }

        return String.valueOf(nivel);
    }
}
