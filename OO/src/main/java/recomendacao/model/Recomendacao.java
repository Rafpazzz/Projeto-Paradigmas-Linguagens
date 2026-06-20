package recomendacao.model;

public class Recomendacao {
    private final Filme filme;
    private final int pontuacao;

    public Recomendacao(Filme filme, int pontuacao) {
        if (filme == null) {
            throw new IllegalArgumentException("Filme recomendado deve ser informado.");
        }

        this.filme = filme;
        this.pontuacao = pontuacao;
    }

    public Filme getFilme() {
        return filme;
    }

    public int getPontuacao() {
        return pontuacao;
    }
}
