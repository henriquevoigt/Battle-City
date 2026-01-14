package ufpel.poo.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class TelaJogo extends JPanel {

    public TelaJogo() {
        
        setBackground(Color.BLACK);
        
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.YELLOW);
        g.drawString("AQUI SERÁ O MAPA DO JOGO", 100, 100);
        g.fillRect(100, 120, 30, 30);
    }
}