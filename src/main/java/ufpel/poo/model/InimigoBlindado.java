package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.view.TelaJogo;

public class InimigoBlindado extends Inimigo {

    public InimigoBlindado(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y, mapa, tela);
        
        this.velocidade = 1; 
        this.vidas = 4;      
        this.pontuacao = 400;
        this.direcao = Direcao.BAIXO;
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            try {
                
                Thread.sleep(2500); 

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
        // muda a cor conforme o dano sofrido
        if (vidas >= 4) g.setColor(new Color(0, 100, 0));       // verde Escuro
        else if (vidas == 3) g.setColor(new Color(50, 150, 50));
        else if (vidas == 2) g.setColor(new Color(100, 200, 100));
        else g.setColor(new Color(150, 250, 150));              // quase morrendo
        
        g.fillRect(x, y, 40, 40);
    }
}