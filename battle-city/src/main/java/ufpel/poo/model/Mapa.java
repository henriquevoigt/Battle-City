package ufpel.poo.model;
import java.io.BufferedReader;
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

    public boolean temColisao(java.awt.Rectangle retanguloTanque) {
        
        for (int i = 0; i < 13; i++) {  // percorre o grid para ver se bate em algo
            for (int j = 0; j < 13; j++) {
                Bloco bloco = grid[i][j];
                
                // se o bloco existe E nao é transponivel
                if (bloco != null && !bloco.ehTransponivel()) {
                    
                    // cria a hitbox
                    java.awt.Rectangle rectBloco = new java.awt.Rectangle(bloco.getX(), bloco.getY(), 40, 40);
                    
                    // verifica se bateu
                    if (rectBloco.intersects(retanguloTanque)) {
                        return true; // bateu
                    }
                }
            }
        }
        return false; // não bateu
    }

    public boolean temColisaoProjetil(java.awt.Rectangle retanguloBala) {
        
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                Bloco bloco = grid[i][j];
                
                if (bloco != null && !bloco.permiteTiro()) {
                    
                    java.awt.Rectangle rectBloco = new java.awt.Rectangle(bloco.getX(), bloco.getY(), 40, 40);
                    
                    if (rectBloco.intersects(retanguloBala)) {
                        return true; 
                    }
                }
            }
        }
        return false; 
    }
}