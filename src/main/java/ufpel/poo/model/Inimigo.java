package ufpel.poo.model;

import ufpel.poo.view.TelaJogo;
import java.awt.Graphics;
import java.util.Random;

public abstract class Inimigo extends Tanque implements Runnable {

    protected Mapa mapa;
    protected TelaJogo tela; 
    protected Random random;
    protected int pontuacao; 

    public Inimigo(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y);
        this.mapa = mapa;
        this.tela = tela; 
        this.random = new Random();
        setDirecao(Direcao.BAIXO); 
    }

    protected void tentarAtirar() {
        if (podeAtirar()) {
            // cria o projétil passando o MAPA e o DONO 
            Projetil p = new Projetil(getX(), getY(), getDirecao(), this, mapa);
            tela.adicionarBala(p);
        }
    }
    
    protected void movimentoAleatorio() {
        int xAntigo = this.x;
        int yAntigo = this.y;

        mover(mapa);

        if (this.x == xAntigo && this.y == yAntigo) {
            mudarDirecaoAleatoria();
        }

        if (random.nextInt(100) < 5) { // 5% de chance
            mudarDirecaoAleatoria();
        }
    }
    
    protected void mudarDirecaoAleatoria() {
        Direcao[] direcoes = Direcao.values();
        setDirecao(direcoes[random.nextInt(direcoes.length)]);
    }

    public abstract int getPontuacao();

    @Override
    public abstract void run();
    
    @Override
    public abstract void desenhar(Graphics g);

}