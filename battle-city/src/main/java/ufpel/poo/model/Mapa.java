package ufpel.poo.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Graphics;

public class Mapa {

    private Bloco[][] grid;
    private final int TAMANHO_BLOCO = 40; 
    
    public Mapa() {
    
        grid = new Bloco[13][13];
        inicializarVazio();
    }

    private void inicializarVazio() {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {
                grid[x][y] = new Vazio(x * TAMANHO_BLOCO, y * TAMANHO_BLOCO);
            }
        }
    }


    public void carregarMapaDeArquivo(String arquivo, int indiceMapa) {

        // inicializa tudo como vazio
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {
                grid[x][y] = new Vazio(x * 40, y * 40);
            }
        }

        try {

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(arquivo);

            if (is == null) {
                System.err.println("Arquivo não encontrado em resources: " + arquivo);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int mapaAtual = 0;
            int linha = 0;
            String texto;

            while ((texto = br.readLine()) != null) {

                if (texto.trim().isEmpty()) {
                    mapaAtual++;
                    linha = 0;
                    continue;
                }

                if (mapaAtual != indiceMapa) continue;

                String[] valores = texto.trim().split("\\s+");

                for (int col = 0; col < 13; col++) {
                    int tipo = Integer.parseInt(valores[col]);
                    grid[col][linha] = BlocoFactory.criar(tipo, col, linha);
                }

                linha++;
                if (linha == 13) break;
            }

            br.close();

        } catch (Exception e) {
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
        }
    }


    private void inicializarMapaTeste() {

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                grid[i][j] = new Vazio(i * TAMANHO_BLOCO, j * TAMANHO_BLOCO);
            }
        }

        
        for (int i = 0; i < 13; i++) {
            grid[i][2] = new Tijolo(i * TAMANHO_BLOCO, 2 * TAMANHO_BLOCO);
            grid[i][8] = new Tijolo(i * TAMANHO_BLOCO, 8 * TAMANHO_BLOCO);
        }

        
        grid[7][4] = new Aco(7 * TAMANHO_BLOCO, 4 * TAMANHO_BLOCO);
        grid[7][5] = new Aco(7 * TAMANHO_BLOCO, 5 * TAMANHO_BLOCO);
        grid[7][6] = new Aco(7 * TAMANHO_BLOCO, 6 * TAMANHO_BLOCO);

        grid[2][5] = new Agua(2 * TAMANHO_BLOCO, 5 * TAMANHO_BLOCO);
        grid[3][5] = new Agua(3 * TAMANHO_BLOCO, 5 * TAMANHO_BLOCO);

        grid[12][5] = new Arvore(12 * TAMANHO_BLOCO, 5 * TAMANHO_BLOCO);
        grid[12][6] = new Arvore(12 * TAMANHO_BLOCO, 6 * TAMANHO_BLOCO);
    }

    public void desenhar(Graphics g) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                grid[i][j].desenhar(g);
            }
        }
    }
    
    
    public Bloco getBloco(int x, int y) {
        if (x >= 0 && x < 13 && y >= 0 && y < 13) {
            return grid[x][y];
        }
        return null;
    }
}

