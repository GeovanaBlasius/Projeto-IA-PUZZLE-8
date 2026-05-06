import java.util.*;

public class Estado {

    public int[][] tabuleiro;

    // Referência para o estado pai (usado para reconstruir o caminho da solução)
    public Estado pai;

    // Profundidade na árvore de busca (quantos movimentos foram feitos desde o estado inicial)
    public int profundidade;

    // configuração final desejada
    private static final int[][] OBJETIVO = {
            {4,5,6},
            {7,0,8},
            {1,2,3}
    };

    // Construtor: cria um novo estado copiando o tabuleiro fornecido
    public Estado(int[][] tabuleiro) {
        this.tabuleiro = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(tabuleiro[i], 0, this.tabuleiro[i], 0, 3);
        }
    }

    // Verifica se o estado atual é igual ao estado objetivo
    public boolean ehObjetivo() {
        return Arrays.deepEquals(tabuleiro, OBJETIVO);
    }

    // Gera os estados vizinhos (movimentos possíveis a partir do espaço vazio)
    public List<Estado> gerarVizinhos() {
        List<Estado> vizinhos = new ArrayList<>();
        int x = 0, y = 0;

        // Localiza a posição do espaço vazio (representado por 0)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }

        // Possíveis direções de movimento: baixo, cima, direita, esquerda
        int[][] direcoes = { {1,0}, {-1,0}, {0,1}, {0,-1} };

        // Para cada direção válida, gera um novo estado vizinho
        for (int[] d : direcoes) {
            int nx = x + d[0];
            int ny = y + d[1];

            // Verifica se a nova posição está dentro dos limites do tabuleiro
            if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                int[][] novo = copiar(tabuleiro);

                // Troca o espaço vazio com a peça vizinha
                novo[x][y] = novo[nx][ny];
                novo[nx][ny] = 0;

                // Cria o novo estado vizinho
                Estado vizinho = new Estado(novo);
                vizinho.pai = this; // define o estado atual como pai
                vizinho.profundidade = this.profundidade + 1; // aumenta a profundidade

                vizinhos.add(vizinho);
            }
        }
        return vizinhos;
    }

    // Metodo auxiliar para copiar o tabuleiro
    private int[][] copiar(int[][] t) {
        int[][] novo = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(t[i], 0, novo[i], 0, 3);
        }
        return novo;
    }

    // Heurística 1: número de peças fora do lugar (g(x))
    public int pecasFora() {
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Conta peças diferentes do objetivo (ignora o espaço vazio)
                if (tabuleiro[i][j] != 0 && tabuleiro[i][j] != OBJETIVO[i][j]) {
                    cont++;
                }
            }
        }
        return cont;
    }

    // Heurística 2: distância de Manhattan (h(x))
    public int distanciaManhattan() {
        int dist = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = tabuleiro[i][j];
                if (val != 0) {
                    // Procura a posição correta da peça no estado objetivo
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (OBJETIVO[x][y] == val) {
                                // Soma a distância em linhas e colunas
                                dist += Math.abs(i - x) + Math.abs(j - y);
                            }
                        }
                    }
                }
            }
        }
        return dist;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Estado)) return false;
        return Arrays.deepEquals(tabuleiro, ((Estado) o).tabuleiro);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tabuleiro);
    }

    // Retorna a profundidade do estado (quantos movimentos desde o inicial)
    public int getProfundidade() {
        return profundidade;
    }
}
