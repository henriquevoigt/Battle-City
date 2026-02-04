package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class InimigoDefault extends Inimigo {

    public InimigoDefault(int x, int y, Mapa mapa) {
        super(x, y, mapa);
        this.velocidade = 2;
        this.vidas = 1;
        this.direcao = Direcao.BAIXO;
    }

    @Override

    public void run() {
        
        // implementar IA

    }

    @Override

    public void desenhar(Graphics g) {
        g.setColor(new Color(220, 220, 220)); // cinza claro
        g.fillRect(x, y, 40, 40);
    }
}