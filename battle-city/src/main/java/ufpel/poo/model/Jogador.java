package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Jogador extends Tanque {

    public Jogador(int x, int y) {

        super(x, y);
        this.velocidade = 2;
        this.vidas = 1;
        this.direcao = Direcao.CIMA;

    }

    @Override

    public void desenhar(Graphics g) {
        
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 40, 40);
        
        g.setColor(Color.BLACK);
        
        int cx = x + 18;
        int cy = y + 18;
        if(direcao == Direcao.CIMA) g.fillRect(cx, y, 4, 20);
        if(direcao == Direcao.BAIXO) g.fillRect(cx, y+20, 4, 20);
        if(direcao == Direcao.ESQUERDA) g.fillRect(x, cy, 20, 4);
        if(direcao == Direcao.DIREITA) g.fillRect(x+20, cy, 20, 4);
    }
}