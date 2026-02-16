package ufpel.poo.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class EntidadeDinamica implements IMovel, IDesenhavel {
    protected int x, y;
    protected int velocidade;
    protected Direcao direcao;

    public EntidadeDinamica(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocidade = 4; // velocidade padrão (pixels por movimento)
        this.direcao = Direcao.CIMA;
    }

    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
    public Direcao getDirecao() { 
        return direcao; 
    }
    
    public void setDirecao(Direcao direcao) { 
        this.direcao = direcao; 
    }

    public void mover(Mapa mapa) {
        
        int novoX = this.x;
        int novoY = this.y;

        
        switch (this.direcao) {
            case CIMA:    novoY -= velocidade; break;
            case BAIXO:   novoY += velocidade; break;
            case ESQUERDA: novoX -= velocidade; break;
            case DIREITA:  novoX += velocidade; break;
        }

        // verificação borda da tela
        if (novoX < 0 || novoX > (520 - 40)) return; 
        if (novoY < 0 || novoY > (520 - 40)) return;

        // hitbox futura, 36px pra não prender nos cantos
        java.awt.Rectangle retanguloFuturo = new java.awt.Rectangle(novoX + 2, novoY + 2, 36, 36);

        // verifica se pode andar
        if (!mapa.temColisao(retanguloFuturo)) {
            this.x = novoX;
            this.y = novoY;
        }
    }

    public Rectangle getLimites() {
        return new Rectangle(x, y, 40, 40);
    }

    public abstract void desenhar(Graphics g);
}