package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Projetil extends EntidadeDinamica {

    private boolean ativo; 

    public Projetil(int x, int y, Direcao direcao) {
        super(x, y);
        this.direcao = direcao;
        this.velocidade = 8; 
        this.ativo = true;
        
        ajustarPosicaoSaida(); // tanque tem 40x40, a bala 6x6
    }

    private void ajustarPosicaoSaida() {
        switch (direcao) {
            case CIMA:      this.x += 17; this.y -= 8;  break;
            case BAIXO:     this.x += 17; this.y += 40; break;
            case ESQUERDA:  this.x -= 8;  this.y += 17; break;
            case DIREITA:   this.x += 40; this.y += 17; break;
        }
    }

    public void moverBala(Mapa mapa) {
        switch (direcao) {
            case CIMA:     y -= velocidade; break;
            case BAIXO:    y += velocidade; break;
            case ESQUERDA: x -= velocidade; break;
            case DIREITA:  x += velocidade; break;
        }

        if (x < -20 || x > 540 || y < -20 || y > 540) { // verifica se saiu da tela
            ativo = false;
            return;
        }
        
        if (mapa.temColisaoProjetil(this.getLimites())) { 
             setAtivo(false);
        }
    }

    public boolean isAtivo(){ 
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public Rectangle getLimites() {
        return new Rectangle(x, y, 6, 6);
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, 6, 6); // Desenha uma bolinha branca
    }
}