package recomendacao.repository;

import recomendacao.model.Classificacao;
import recomendacao.model.Filme;
import recomendacao.model.Genero;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CatalogoFilmes {
    private final Path caminhoArquivo;

    public CatalogoFilmes(Path caminhoArquivo) {
        if (caminhoArquivo == null) {
            throw new IllegalArgumentException("Caminho do arquivo de filmes deve ser informado.");
        }

        this.caminhoArquivo = caminhoArquivo;
    }

    public List<Filme> listarTodos() throws IOException {
        List<Filme> filmes = new ArrayList<>();
        List<String> linhas = Files.readAllLines(caminhoArquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();

            if (!linha.isEmpty()) {
                filmes.add(criarFilme(linha));
            }
        }

        return List.copyOf(filmes);
    }

    private Filme criarFilme(String linha) {
        List<String> campos = separarCamposCsv(linha);

        if (campos.size() != 5) {
            throw new IllegalArgumentException("Linha de filme invalida: " + linha);
        }

        String titulo = campos.get(1);
        List<Genero> generos = criarGeneros(campos.get(2));
        int duracaoMinutos = Integer.parseInt(campos.get(3));
        Classificacao classificacao = Classificacao.fromDescricao(campos.get(4));

        return new Filme(titulo, generos, duracaoMinutos, classificacao);
    }

    private List<Genero> criarGeneros(String generosTexto) {
        List<Genero> generos = new ArrayList<>();

        for (String generoTexto : generosTexto.split(",")) {
            generos.add(Genero.fromDescricao(generoTexto));
        }

        return generos;
    }

    private List<String> separarCamposCsv(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoAtual = new StringBuilder();
        boolean dentroDeAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char caractere = linha.charAt(i);

            if (caractere == '"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (caractere == ',' && !dentroDeAspas) {
                campos.add(campoAtual.toString().trim());
                campoAtual.setLength(0);
            } else {
                campoAtual.append(caractere);
            }
        }

        campos.add(campoAtual.toString().trim());
        return campos;
    }
}
