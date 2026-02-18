package ufpel.poo.model;

import java.awt.Color;

public enum TipoPowerUp {
    VIDA(Color.PINK),      
    ESTRELA(Color.YELLOW), 
    BOMBA(Color.RED),      
    RELOGIO(Color.CYAN),   
    CAPACETE(Color.GREEN),
    PA(Color.GRAY);        

    private Color cor;

    TipoPowerUp(Color cor) {
        this.cor = cor;
    }

    public Color getCor() {
        return cor;
    }
}