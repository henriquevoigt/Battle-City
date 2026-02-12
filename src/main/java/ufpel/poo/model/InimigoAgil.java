package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.view.TelaJogo;

public class InimigoAgil extends Inimigo {

    public InimigoAgil(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y, mapa, tela);
        
        
        this.velocidade = 4; 
        this.vidas = 1;      
        this.pontuacao = 200;
        this.direcao = Direcao.BAIXO;
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            try {
                Thread.sleep(1500); 

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
        g.setColor(new Color(100, 255, 100)); // verde claro
        g.fillRect(x, y, 40, 40);
    }
}