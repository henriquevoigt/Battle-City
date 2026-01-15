package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Tanque extends EntidadeDinamica {
    
    public Tanque(int x, int y) {
        super(x, y);
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.YELLOW);
        
        g.fillRect(x + 5, y + 5, 30, 30);
        
        g.setColor(new Color(100, 100, 0)); 
        g.fillRect(x, y + 5, 5, 30);  
        g.fillRect(x + 35, y + 5, 5, 30); 
        
        g.setColor(Color.WHITE);
        int canhaoL = 4; 
        int canhaoC = 18;
        
        switch (direcao) {
            case CIMA:
                g.fillRect(x + 18, y - 2, canhaoL, canhaoC);
                break;
            case BAIXO:
                g.fillRect(x + 18, y + 20, canhaoL, canhaoC);
                break;
            case ESQUERDA:
                g.fillRect(x - 2, y + 18, canhaoC, canhaoL);
                break;
            case DIREITA:
                g.fillRect(x + 20, y + 18, canhaoC, canhaoL);
                break;
        }
    }
}