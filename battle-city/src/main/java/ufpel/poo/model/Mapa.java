package ufpel.poo.model;

import java.awt.Graphics;

public class Mapa {

    private Bloco[][] grid;
    private final int TAMANHO_BLOCO = 40; 
    
    public Mapa() {
    
        grid = new Bloco[13][13];
        inicializarMapaTeste();
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