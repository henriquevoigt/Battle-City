package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class InimigoBlindado extends Inimigo {

    public InimigoBlindado(int x, int y, Mapa mapa) {
        super(x, y, mapa);
        this.velocidade = 1;
        this.vidas = 4;
        this.direcao = Direcao.BAIXO;
    }

    @Override
    public void run() {
        
        // implementar logica de IA
        
    }

    @Override

    public void desenhar(Graphics g) {
        g.setColor(new Color(128, 128, 128)); // cinza escuro
        g.fillRect(x, y, 40, 40);
    }
}