package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

import ufpel.poo.view.TelaJogo;

public class InimigoDefault extends Inimigo {
    private TelaJogo tela;

    public InimigoDefault(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y, mapa);
        this.tela = tela;
        this.velocidade = 2;
        this.vidas = 1;
        this.direcao = Direcao.BAIXO;
    }

    @Override

    public void run() {
        while (true) {
            try {
                Thread.sleep(2000); // atira a cada 2s
            } catch (InterruptedException e) {}

            Projetil p = new Projetil(this.x, this.y, Direcao.BAIXO, false);
            tela.adicionarBala(p);

        }
        
        // implementar IA

    }

    @Override

    public void desenhar(Graphics g) {
        g.setColor(new Color(220, 220, 220)); // cinza claro
        g.fillRect(x, y, 40, 40);
    }
}