package recomendacao.app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import recomendacao.criterio.CriterioClassificacao;
import recomendacao.criterio.CriterioDuracao;
import recomendacao.criterio.CriterioGenero;
import recomendacao.criterio.CriterioHumor;
import recomendacao.model.Classificacao;
import recomendacao.model.Filme;
import recomendacao.model.Genero;
import recomendacao.model.Humor;
import recomendacao.model.PerfilUsuario;
import recomendacao.model.Recomendacao;
import recomendacao.repository.CatalogoFilmes;
import recomendacao.service.MotorRecomendacao;

public class Main {
    private static final Path CAMINHO_ARQUIVO_FILMES = Path.of("OO/src/filmes");

    public static void main(String[] args) throws IOException {
        PerfilUsuario perfil = new PerfilUsuario(
                EnumSet.of(Genero.ACAO, Genero.FICCAO_CIENTIFICA),
                Humor.ANIMADO,
                150,
                Classificacao.QUATORZE
        );

        CatalogoFilmes catalogo = new CatalogoFilmes(CAMINHO_ARQUIVO_FILMES);
        MotorRecomendacao motor = new MotorRecomendacao(List.of(
                new CriterioGenero(),
                new CriterioDuracao(),
                new CriterioClassificacao(),
                new CriterioHumor()
        ));

        List<Recomendacao> recomendacoes = motor.recomendar(catalogo.listarTodos(), perfil);

        for (Recomendacao recomendacao : recomendacoes) {
            System.out.println(formatar(recomendacao));
        }
    }

    private static String formatar(Recomendacao recomendacao) {
        Filme filme = recomendacao.getFilme();
        String generos = filme.getGeneros().stream()
                .map(Genero::getDescricao)
                .collect(Collectors.joining(" + "));

        return String.format(
                "[%d pts] %s (%s, %d min, %s)",
                recomendacao.getPontuacao(),
                filme.getTitulo(),
                generos,
                filme.getDuracaoMinutos(),
                filme.getClassificacao().getDescricao()
        );
    }
}
