package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Tijolo extends Bloco {
    private boolean destruido = false;

    public Tijolo(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return destruido;
    }

    @Override
    public void receberImpacto(int forca) {
        if (forca > 0) {
            this.destruido = true;
        }
    }

    @Override
    public void desenhar(Graphics g) {
        if (!destruido) {
            g.setColor(new Color(184, 87, 51));
            g.fillRect(x, y, 40, 40);
            
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 40, 40);
        }
    }

    @Override
    public boolean permiteTiro() { 
        return false;
    }

}