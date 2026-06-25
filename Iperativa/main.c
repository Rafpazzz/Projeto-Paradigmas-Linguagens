#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAX_GENEROS 2
#define TOTAL_FILMES 12

typedef struct {
	char titulo[50];
	char generos[MAX_GENEROS][30];
	int qtd_generos;
	int duracao;
	int classificacao;
} Filme;

typedef struct {
	char generos_favoritos[5][30];
	int qtd_favoritos;
	char humor[20];
	int duracao_max;
	int classificacao;
} PerfilUsuario;

typedef struct {
	Filme filme;
	int pontuacao;
} Resultado;

Filme catalogo[TOTAL_FILMES] = {
	{"Duna", {"ficcao_cientifica"}, 1, 155, 12},
	{"Vingadores", {"acao"}, 1, 149, 12},
	{"Forrest Gump", {"drama"}, 1, 142, 0},
	{"Superbad", {"comedia"}, 1, 113, 18},
	{"La La Land", {"romance"}, 1, 128, 0},
	{"Mad Max", {"acao"}, 1, 120, 14},
	{"Her", {"ficcao_cientifica","drama"}, 2, 126, 14},
	{"Comer Rezar Amar", {"romance"}, 1, 133, 0},
	{"Tropa de Elite", {"acao","drama"}, 2, 115, 18},
	{"Interestelar", {"ficcao_cientifica"}, 1, 169, 0},
	{"Clueless", {"comedia","romance"}, 2, 97, 14},
	{"O Poderoso Chefão", {"drama"}, 1, 175, 14}
};

int classif_ok(int filme, int usuario) {
	return filme <= usuario;
}

int contem_genero(const char generos[][30], int qtd, const char *genero) {
	int i;

	for(i = 0; i < qtd; i++) {
		if(strcmp(generos[i], genero) == 0)
			return 1;
	}

	return 0;
}

void generos_humor(const char *humor, char prioridade[][30], int *qtd) {

	if(strcmp(humor, "animado") == 0) {
		strcpy(prioridade[0], "acao");
		strcpy(prioridade[1], "comedia");
		*qtd = 2;
	}
	else if(strcmp(humor, "reflexivo") == 0) {
		strcpy(prioridade[0], "drama");
		strcpy(prioridade[1], "ficcao_cientifica");
		*qtd = 2;
	}
	else {
		strcpy(prioridade[0], "comedia");
		strcpy(prioridade[1], "romance");
		*qtd = 2;
	}
}

int calcular_pontuacao(Filme filme, PerfilUsuario usuario) {

	int pontos = 0;
	int i;
	int j;

	for(i = 0; i < filme.qtd_generos; i++) {
		for(j = 0; j < usuario.qtd_favoritos; j++) {
			if(strcmp(filme.generos[i],
			          usuario.generos_favoritos[j]) == 0) {
				pontos++;
			}
		}
	}

	char prioridade[2][30];
	int qtd_prioridade;

	generos_humor(usuario.humor,
	              prioridade,
	              &qtd_prioridade);

	for(i = 0; i < filme.qtd_generos; i++) {

		if(contem_genero(prioridade,qtd_prioridade, filme.generos[i])) {
			pontos++;
			break;
		}
	}

	return pontos;
}

int possui_genero_favorito(Filme filme, PerfilUsuario usuario) {
	int i;
	int j;

	for(i = 0; i < filme.qtd_generos; i++) {
		for(j = 0; j < usuario.qtd_favoritos; j++) {
			if(strcmp(filme.generos[i], usuario.generos_favoritos[j]) == 0) {
				return 1;
			}
		}
	}

	return 0;
}

int comparar(const void *a, const void *b) {

	Resultado *r1 = (Resultado*)a;
	Resultado *r2 = (Resultado*)b;

	if(r2->pontuacao != r1->pontuacao)
		return r2->pontuacao - r1->pontuacao;

	return strcmp(r1->filme.titulo, r2->filme.titulo);
}

const char* texto_classificacao(int classificacao, char buffer[10]) {
	if(classificacao == 0)
		return "livre";

	sprintf(buffer, "%d", classificacao);
	return buffer;
}

void texto_generos(const char generos[][30], int qtd, char buffer[120]) {
	int i;

	if(qtd == 0) {
		strcpy(buffer, "nenhum");
		return;
	}

	buffer[0] = '\0';

	for(i = 0; i < qtd; i++) {
		if(i > 0)
			strcat(buffer, ", ");

		strcat(buffer, generos[i]);
	}
}

void imprimir_tracos(int qtd) {
	int i;

	for(i = 0; i < qtd; i++)
		putchar('-');

	putchar('\n');
}

void imprimir_cabecalho(const char *cabecalho) {
	printf("%s\n", cabecalho);
	imprimir_tracos(strlen(cabecalho));
}

void recomendar(PerfilUsuario usuario) {

	Resultado resultados[50];
	int qtd = 0;
	int i;

	for(i = 0; i < TOTAL_FILMES; i++) {

		Filme filme = catalogo[i];
		
		if(!possui_genero_favorito(filme, usuario))
			continue;
		if(filme.duracao > usuario.duracao_max)
			continue;
		if(!classif_ok(filme.classificacao, usuario.classificacao))
			continue;
		
		int pontos = calcular_pontuacao(filme, usuario);
		
		if(pontos >= 2) {
			resultados[qtd].filme = filme;
			resultados[qtd].pontuacao = pontos;
			qtd++;
		}
	}

	qsort(resultados, qtd, sizeof(Resultado), comparar);

    if (qtd == 0){
        printf("Nenhum filme recomendado.\n\n");
        return;
    }

	for(i = 0; i < qtd; i++) {
		char generos_filme[120];
		char classificacao_filme[10];

		texto_generos(resultados[i].filme.generos,
		              resultados[i].filme.qtd_generos,
		              generos_filme);

		printf("[%d pts] %s (%s, %d min, %s)\n",
		       resultados[i].pontuacao,
		       resultados[i].filme.titulo,
		       generos_filme,
		       resultados[i].filme.duracao,
		       texto_classificacao(resultados[i].filme.classificacao,
		                           classificacao_filme));
	}
	
	printf("\n");
}

int main() {

	PerfilUsuario usuario1, usuario2, usuario3;

    imprimir_cabecalho("USUARIO 01 (generos: acao, ficcao_cientifica | humor: Animado | duracao max: 150 min | classificacao: 14)");
	strcpy(usuario1.generos_favoritos[0], "acao");
	strcpy(usuario1.generos_favoritos[1], "ficcao_cientifica");
	usuario1.qtd_favoritos = 2;
	strcpy(usuario1.humor, "animado");
	usuario1.duracao_max = 150;
	usuario1.classificacao = 14;

	recomendar(usuario1);
	
	imprimir_cabecalho("USUARIO 02 (generos: drama, romance | humor: Reflexivo | duracao max: 180 min | classificacao: 18)");
	strcpy(usuario2.generos_favoritos[0], "drama");
	strcpy(usuario2.generos_favoritos[1], "romance");
	usuario2.qtd_favoritos = 2;
	strcpy(usuario2.humor, "reflexivo");
	usuario2.duracao_max = 180;
	usuario2.classificacao = 18;

	recomendar(usuario2);
	
	imprimir_cabecalho("USUARIO 03 (generos: nenhum | humor: Triste | duracao max: 140 min | classificacao: livre)");
	usuario3.qtd_favoritos = 0;
	strcpy(usuario3.humor, "triste");
	usuario3.duracao_max = 140;
	usuario3.classificacao = 0;

	recomendar(usuario3);
	
	return 0;
}
