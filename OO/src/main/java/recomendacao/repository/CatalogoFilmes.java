package recomendacao.repository;

import recomendacao.model.ClassificacaoEtaria;
import recomendacao.model.Filme;

import java.util.List;

public class CatalogoFilmes {
    public List<Filme> listarFilmes() {
        return List.of(
                new Filme("Duna", List.of("ficcao_cientifica"), 155, ClassificacaoEtaria.DOZE),
                new Filme("Vingadores", List.of("acao"), 149, ClassificacaoEtaria.DOZE),
                new Filme("Forrest Gump", List.of("drama"), 142, ClassificacaoEtaria.LIVRE),
                new Filme("Superbad", List.of("comedia"), 113, ClassificacaoEtaria.DEZOITO),
                new Filme("La La Land", List.of("romance"), 128, ClassificacaoEtaria.LIVRE),
                new Filme("Mad Max", List.of("acao"), 120, ClassificacaoEtaria.CATORZE),
                new Filme("Her", List.of("ficcao_cientifica", "drama"), 126, ClassificacaoEtaria.CATORZE),
                new Filme("Comer Rezar Amar", List.of("romance"), 133, ClassificacaoEtaria.LIVRE),
                new Filme("Tropa de Elite", List.of("acao", "drama"), 115, ClassificacaoEtaria.DEZOITO),
                new Filme("Interestelar", List.of("ficcao_cientifica"), 169, ClassificacaoEtaria.LIVRE),
                new Filme("Clueless", List.of("comedia", "romance"), 97, ClassificacaoEtaria.CATORZE),
                new Filme("O Poderoso Chefão", List.of("drama"), 175, ClassificacaoEtaria.CATORZE)
        );
    }
}
