package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.view.TelaJogo;

public class InimigoDefault extends Inimigo {

    public InimigoDefault(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y, mapa, tela); 
                
        this.velocidade = 2;
        this.vidas = 1;
        this.pontuacao = 100;
        this.direcao = Direcao.BAIXO;
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            try {
                Thread.sleep(2000); 

                if (this.podeAtirar()) {
                    Projetil p = new Projetil(this.x, this.y, this.direcao, this);
                    tela.adicionarBala(p);
                }
                
                // implementar logica de movimento aqui

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(new Color(220, 220, 220)); // cinza claro
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);

        if (direcao == Direcao.BAIXO) {
            g.fillRect(x+18, y+20, 4, 20);
        } else if (direcao == Direcao.CIMA) {
            g.fillRect(x+18, y, 4, 20);
        } else if (direcao == Direcao.ESQUERDA) {
            g.fillRect(x, y+18, 20, 4);
        } else if (direcao == Direcao.DIREITA) {
            g.fillRect(x+20, y+18, 20, 4);
        }
    }
}