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
    public void receberImpacto(int forca) {
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 40, 40);
        
        g.setColor(Color.CYAN);
        g.drawLine(x + 5, y + 10, x + 35, y + 10);
        g.drawLine(x + 5, y + 20, x + 35, y + 20);
        g.drawLine(x + 5, y + 30, x + 35, y + 30);
    }

    @Override
    public boolean permiteTiro() { 
        return true;
    }

}