package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import ufpel.poo.controller.MotorFisica;

public class Projetil extends EntidadeDinamica implements Runnable {

    private boolean doJogador;
    private volatile boolean ativo; 
    private Tanque dono; 
    private MotorFisica motor; 
    private Thread thread; 
    private int dano;

    public Projetil(int x, int y, Direcao direcao, Tanque dono, MotorFisica motor) {
        this(x, y, direcao, dono, motor, 1); 
    }

    public Projetil(int x, int y, Direcao direcao, Tanque dono, MotorFisica motor, int dano) {
        super(x, y);
        this.direcao = direcao;
        this.dono = dono;
        this.motor = motor;
        this.dano = dano; 
        
        this.velocidade = 6;
        this.ativo = true;
        this.doJogador = (dono instanceof Jogador);

        if (this.dono != null) {
            this.dono.registrarDisparo();
        }
        
        ajustarPosicaoSaida(); 
        
        this.thread = new Thread(this);
        this.thread.start();
    }

    private void ajustarPosicaoSaida() {
        switch (direcao) {
            case CIMA:      this.x += 17; this.y -= 8;  break;
            case BAIXO:     this.x += 17; this.y += 40; break;
            case ESQUERDA:  this.x -= 8;  this.y += 17; break;
            case DIREITA:   this.x += 40; this.y += 17; break;
        }
    }

    @Override
    public void run() {
        while (ativo) {
            if (motor.isJogoPausado()) {
                try { 
                    Thread.sleep(100);
                } catch (InterruptedException e) { 
                    e.printStackTrace(); 
                }
                continue; 
            }

            mover();
            
            try {
                Thread.sleep(16); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void mover() {
        if (!ativo) return;

        switch (direcao) {
            case CIMA:     y -= velocidade; break;
            case BAIXO:    y += velocidade; break;
            case ESQUERDA: x -= velocidade; break;
            case DIREITA:  x += velocidade; break;
        }

        if (x < 0 || x > 514 || y < 0 || y > 514) { 
            setAtivo(false); 
            return;
        }

        motor.processarMovimentoBala(this);

        if (!ativo) return;
    }

    public boolean isAtivo(){ 
        return ativo;
    }
    
    public boolean ehDoJogador() {
        return doJogador;
    }

    public int getDano() {
        return dano;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
        if (!ativo && dono != null) {
            dono.recarregar();
        }
    }

    @Override
    public Rectangle getLimites() {
        return new Rectangle(x, y, 6, 6);
    }

    @Override
    public void desenhar(Graphics g) {
        if (doJogador) {
            g.setColor(Color.WHITE); 
        } else {
            g.setColor(Color.RED); 
        }
        g.fillOval(x, y, 6, 6); 
    }
}