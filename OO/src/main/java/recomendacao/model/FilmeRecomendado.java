package recomendacao.model;

public class FilmeRecomendado {
    private final Filme filme;
    private final int pontuacao;

    public FilmeRecomendado(Filme filme, int pontuacao) {
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
