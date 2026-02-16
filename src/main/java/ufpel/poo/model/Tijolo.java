package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Tijolo extends Bloco {

    public Tijolo(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return false;
    }

    @Override
    public boolean receberDano(int dano) {
        return true;
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(new Color(184, 87, 51));
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 40, 40);
        g.drawLine(x, y+20, x+40, y+20);
        g.drawLine(x+20, y, x+20, y+20);
        g.drawLine(x+10, y+20, x+10, y+40);
    }

    @Override
    public boolean permiteTiro() { 
        return false;
    }
}