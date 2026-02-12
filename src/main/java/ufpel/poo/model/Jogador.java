package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Jogador extends Tanque {
    
    private int estoqueVidas; 
    
    private int xInicial, yInicial; 

    public Jogador(int x, int y) {
        super(x, y);
        this.xInicial = x;
        this.yInicial = y;
        
        this.velocidade = 4;
        this.vidas = 1;     
        this.estoqueVidas = 2; 
        this.direcao = Direcao.CIMA;
    }

    public boolean tentarRespawn() {
        if (estoqueVidas > 0) {
            estoqueVidas--; 
            
            this.vidas = 1; 
            this.x = xInicial; 
            this.y = yInicial;
            this.direcao = Direcao.CIMA;
            this.balasAtivas = 0; 
            
            return true; 
        }
        return false; 
    }
    
    public int getEstoqueVidas() {
        return estoqueVidas;
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);
    }
}