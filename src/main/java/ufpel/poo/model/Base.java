package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Base extends Bloco {

    private boolean destruida = false;

    public Base(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean ehTransponivel() {
        return false; 
    }

    @Override
    public boolean receberDano(int dano) {
        this.destruida = true;
        return false; 
    }

    @Override
    public boolean permiteTiro() {
        return false; 
    }

    public boolean isDestruida() {
        return destruida;
    }

    @Override
    public void desenhar(Graphics g) {
        if (destruida) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, 40, 40);
            g.setColor(Color.RED);
            g.drawLine(x, y, x + 40, y + 40);
            g.drawLine(x + 40, y, x, y + 40);
        } else {
            g.setColor(new Color(255, 215, 0)); 
            g.fillRect(x, y, 40, 40);
            g.setColor(new Color(139, 69, 19)); 
            g.fillRect(x + 10, y + 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 40, 40);
            g.setColor(Color.WHITE);
            g.fillRect(x + 15, y + 15, 5, 5);
        }
    }
}