package ufpel.poo.model;

import java.awt.Graphics;

public abstract class Bloco {
    protected int x;
    protected int y;

    public Bloco(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean ehTransponivel();

    public abstract void receberImpacto(int forca); // (força 1 = normal, 2 = powerup)

    public abstract boolean permiteTiro();

    public abstract void desenhar(Graphics g);

    public int getX() { return x; }
    public int getY() { return y; }
}