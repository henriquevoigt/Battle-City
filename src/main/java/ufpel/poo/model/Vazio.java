package ufpel.poo.model;

import java.awt.Graphics;

public class Vazio extends Bloco {

    public Vazio(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return true; 
    }

    @Override
    public boolean receberDano(int dano) {
        return false; 
    }

    @Override
    public void desenhar(Graphics g) {
    }

    @Override
    public boolean permiteTiro() { 
        return true; 
    }
    
}