package ufpel.poo.model;

import ufpel.poo.controller.MotorFisica;

import java.awt.Graphics;
import java.util.Random;
public abstract class Inimigo extends Tanque implements Runnable {

    protected Mapa mapa;
    protected MotorFisica motor;
    protected Random random;
    protected int pontuacao; 
    protected boolean congelado = false;

    public Inimigo(int x, int y, Mapa mapa, MotorFisica motor) {
        super(x, y);
        this.mapa = mapa;
        this.motor = motor; 
        this.random = new Random();
        setDirecao(Direcao.BAIXO); 
    }

    protected void tentarAtirar() {
        if (podeAtirar()) {
            Projetil p = new Projetil(getX(), getY(), getDirecao(), this, motor);
            motor.adicionarProjetil(p);
        }
    }
    
    protected void movimentoAleatorio() {
        int xAntigo = this.x;
        int yAntigo = this.y;

        mover(motor);

        if (this.x == xAntigo && this.y == yAntigo) {
            mudarDirecaoAleatoria();
        }

        if (random.nextInt(100) < 5) { 
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

    public void setCongelado(boolean congelado) {
        this.congelado = congelado;
    }

    protected boolean deveFicarParado() {
        if (motor.isJogoPausado()) {
            dormir(100);
            return true;
        }

        if (congelado) {
            dormir(100);
            return true;
        }
        return false; 
    }

    protected void dormir(int ms) {
        try { 
            Thread.sleep(ms); 
        } 
        catch (Exception e) {}
    }
}