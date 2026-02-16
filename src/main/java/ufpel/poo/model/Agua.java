package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Agua extends Bloco {

    public Agua(int x, int y) { 
        super(x, y); 
    }

    @Override
    public boolean ehTransponivel() { 
        return false; 
    } 

    @Override
    public boolean receberDano(int dano) {
        return false; 
    } 

    @Override
    public boolean permiteTiro() { 
        return true; 
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 40, 40);
        g.setColor(new Color(100, 100, 255));
        g.drawLine(x, y+10, x+40, y+10);
        g.drawLine(x, y+30, x+40, y+30);
    }
}