package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.controller.MotorFisica;

public class InimigoAgil extends Inimigo {

    public InimigoAgil(int x, int y, Mapa mapa, MotorFisica motor) {
        super(x, y, mapa, motor);
        this.velocidade = 3; 
        setVidas(1); 
        this.pontuacao = 200;
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

            if (random.nextInt(100) < 2) {
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
        g.setColor(Color.CYAN); 
        g.fillRect(x, y, 40, 40);
        g.setColor(Color.BLACK);
        if (direcao == Direcao.BAIXO) g.fillRect(x+18, y+20, 4, 20);
        else if (direcao == Direcao.CIMA) g.fillRect(x+18, y, 4, 20);
        else if (direcao == Direcao.ESQUERDA) g.fillRect(x, y+18, 20, 4);
        else if (direcao == Direcao.DIREITA) g.fillRect(x+20, y+18, 20, 4);
    }
}