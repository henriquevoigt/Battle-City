package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Arvore extends Bloco {

    public Arvore(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return true;
    }

    @Override
    public void receberImpacto(int forca) {
    }

    @Override
    public void desenhar(Graphics g) {

        g.setColor(new Color(0, 100, 0));
        g.fillRect(x, y, 40, 40);
        
        g.setColor(new Color(34, 139, 34)); 
        g.fillOval(x + 5, y + 5, 15, 15);
        g.fillOval(x + 20, y + 5, 15, 15);
        g.fillOval(x + 10, y + 20, 20, 20);
    }

    @Override
    public boolean permiteTiro() { 
        return true;
    }

}