package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.controller.MotorFisica;

public class InimigoAgil extends Inimigo {

    private int contadorPassos = 0;

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

    private void perseguirJogador() {
        Jogador jogador = motor.getJogador();

        if (jogador == null || !jogador.estaVivo()) {
            movimentoAleatorio();
            return;
        }

        contadorPassos++;

        if (contadorPassos > 30) {
            int distX = jogador.getX() - this.x;
            int distY = jogador.getY() - this.y;

            if (Math.abs(distX) > Math.abs(distY)) {
                setDirecao(distX > 0 ? Direcao.DIREITA : Direcao.ESQUERDA);
            } else {
                setDirecao(distY > 0 ? Direcao.BAIXO : Direcao.CIMA);
            }
            contadorPassos = 0;
        }

        int xAntigo = this.x;
        int yAntigo = this.y;

        mover(motor);

        if (this.x == xAntigo && this.y == yAntigo) {
            mudarDirecaoAleatoria();

            contadorPassos = -30; 
        }
    }

    @Override
    public void run() {
        while (this.estaVivo()) {
            if (deveFicarParado()) {
                continue; 
            }

            perseguirJogador();

            if (random.nextInt(100) < 3) {
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