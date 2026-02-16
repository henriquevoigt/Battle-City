package ufpel.poo.model;

import java.awt.Graphics;

public abstract class Bloco implements IDesenhavel, IDestrutivel {
    protected int x;
    protected int y;

    public Bloco(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean ehTransponivel();

    public abstract boolean receberDano(int forca); 

    public abstract boolean permiteTiro();

    public abstract void desenhar(Graphics g);

    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
}