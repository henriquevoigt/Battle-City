package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.controller.MotorFisica;

public class InimigoDefault extends Inimigo {

    public InimigoDefault(int x, int y, Mapa mapa, MotorFisica motor) {
        super(x, y, mapa, motor);
        this.velocidade = 2;
        setVidas(1);
        this.pontuacao = 100;
    }

    @Override 
    public int getPontuacao() { 
        return this.pontuacao; 
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            if (deveFicarParado()) {
                continue; 
            }
            movimentoAleatorio();

            if (random.nextInt(100) < 1) {
                tentarAtirar();
            }
            try { 
                Thread.sleep(16); 
            } 
            catch (InterruptedException e) {
                break; 
            }
        }
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);
        if (direcao == Direcao.BAIXO) g.fillRect(x+18, y+20, 4, 20);
        else if (direcao == Direcao.CIMA) g.fillRect(x+18, y, 4, 20);
        else if (direcao == Direcao.ESQUERDA) g.fillRect(x, y+18, 20, 4);
        else if (direcao == Direcao.DIREITA) g.fillRect(x+20, y+18, 20, 4);
    }
}