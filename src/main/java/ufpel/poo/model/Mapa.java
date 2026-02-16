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

        inicializarVazio();

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(arquivo);

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
                    if (col < valores.length) {
                        int tipo = Integer.parseInt(valores[col]);
                        grid[col][linha] = BlocoFactory.criar(tipo, col, linha);
                    }
                }

                linha++;
                if (linha == 13) break;
            }
            br.close();

        } catch (Exception e) {
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void desenharFundo(Graphics g) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (!(grid[i][j] instanceof Arvore)) {
                    grid[i][j].desenhar(g);
                }
            }
        }
    }

    public void desenharTopo(Graphics g) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (grid[i][j] instanceof Arvore) {
                    grid[i][j].desenhar(g);
                }
            }
        }
    }

    public boolean temColisao(java.awt.Rectangle retanguloTanque) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                Bloco bloco = grid[i][j];

                if (bloco != null && !bloco.ehTransponivel()) {
                    
                    java.awt.Rectangle rectBloco = new java.awt.Rectangle(bloco.getX(), bloco.getY(), 40, 40);
                    
                    if (rectBloco.intersects(retanguloTanque)) {
                        return true; 
                    }
                }
            }
        }
        return false; 
    }

    public boolean processarColisaoProjetil(java.awt.Rectangle retanguloBala) {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {
                Bloco bloco = grid[x][y];
                
                if (bloco != null && !bloco.permiteTiro()) {
                    
                    java.awt.Rectangle rectBloco = new java.awt.Rectangle(bloco.getX(), bloco.getY(), 40, 40);
                    
                    if (rectBloco.intersects(retanguloBala)) {
                        boolean foiDestruido = bloco.receberDano(1); 
                        
                        if (foiDestruido) {
                            grid[x][y] = new Vazio(bloco.getX(), bloco.getY());
                        }
                        return true; // bala bateu em algo e deve sumir
                    }
                }
            }
        }
        return false; 
    }

    public boolean verificarGameOver() {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 13; y++) {
                if (grid[x][y] instanceof Base) {
                    if (((Base) grid[x][y]).isDestruida()) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }
}