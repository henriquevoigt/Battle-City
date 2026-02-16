package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Aco extends Bloco {

    public Aco(int x, int y) { 
        super(x, y); 
    }

    @Override
    public boolean ehTransponivel() { 
        return false; 
    } 

    @Override
    public boolean receberDano(int dano) {
        if (dano > 1) {
            return true; 
        }
        return false;
    }

    @Override
    public boolean permiteTiro() { 
        return false; 
    } 

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.WHITE); 
        g.drawRect(x+5, y+5, 30, 30);
    }
}