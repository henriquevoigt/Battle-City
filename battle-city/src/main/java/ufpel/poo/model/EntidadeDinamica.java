package ufpel.poo.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class EntidadeDinamica {
    protected int x, y;
    protected int velocidade;
    protected Direcao direcao;

    public EntidadeDinamica(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocidade = 4; 
        this.direcao = Direcao.CIMA; 
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Direcao getDirecao() { return direcao; }
    public void setDirecao(Direcao direcao) { this.direcao = direcao; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }

    public abstract void desenhar(Graphics g);
}