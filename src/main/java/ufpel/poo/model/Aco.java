package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Aco extends Bloco {
    private boolean destruido = false;

    public Aco(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return destruido;
    }

    @Override
    public void receberImpacto(int forca) {
        if (forca > 1) {
            this.destruido = true;
        }
    }

    @Override
    public void desenhar(Graphics g) {
        if (!destruido) {
            g.setColor(Color.LIGHT_GRAY); 
            g.fillRect(x, y, 40, 40);
            
            g.setColor(Color.WHITE);
            g.drawRect(x + 10, y + 10, 20, 20);
        }
    }

    @Override
    public boolean permiteTiro() { 
        return false; 
    }

}