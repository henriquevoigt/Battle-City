package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class InimigoAgil extends Inimigo {

    public InimigoAgil(int x, int y, Mapa mapa) {
        super(x, y, mapa);
        this.velocidade = 4;
        this.vidas = 1;
        this.direcao = Direcao.BAIXO;
    }

    @Override
    public void run() {
        
        // implementar logica de IA
        
    }

    @Override

    public void desenhar(Graphics g) {
        g.setColor(new Color(95, 158, 160)); // verde
        g.fillRect(x, y, 40, 40);
    }
}