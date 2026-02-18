package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.controller.MotorFisica;

public class InimigoBlindado extends Inimigo {

    public InimigoBlindado(int x, int y, Mapa mapa, MotorFisica motor) {
        super(x, y, mapa, motor);           
        this.velocidade = 1; 
        setVidas(4); 
        this.pontuacao = 400;
    }

    @Override
    public int getPontuacao() {
        return this.pontuacao;
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            if (motor.isJogoPausado()) {
                try { Thread.sleep(100); } catch (Exception e) {}
                continue;
            }

            movimentoAleatorio();
 
            if (random.nextInt(100) < 1) { // 1% de chance 
                tentarAtirar();
            }
            try {
                Thread.sleep(16); // 60 FPS
            } 
            catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(new Color(220, 220, 220)); // Cinza Blindado
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);
        if (direcao == Direcao.BAIXO)      g.fillRect(x+18, y+20, 4, 20);
        else if (direcao == Direcao.CIMA)  g.fillRect(x+18, y, 4, 20);
        else if (direcao == Direcao.ESQUERDA) g.fillRect(x, y+18, 20, 4);
        else if (direcao == Direcao.DIREITA)  g.fillRect(x+20, y+18, 20, 4);
    }
}