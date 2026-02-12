package ufpel.poo.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Tanque extends EntidadeDinamica {

    protected int vidas;
    protected int pontuacao;   
    protected int balasAtivas = 0;

    public Tanque(int x, int y) {
        super(x, y);
        this.velocidade = 0;
        this.vidas = 1;
        this.pontuacao = 0;    
    }

    public boolean podeAtirar() {
        return balasAtivas < 1;
    }

    public void registrarDisparo() {
        balasAtivas++;
    }

    public void recarregar() {
        if (balasAtivas > 0) {
            balasAtivas--;
        }
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontos) {
        this.pontuacao = pontos;
    }

    public void receberDano() {
        if(vidas > 0){
            this.vidas--;
        }
    }

    public int getVidas() {
        return vidas;
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    @Override
    public Rectangle getLimites() {
        return new Rectangle(x, y, 40, 40);
    }

    public abstract void desenhar(Graphics g);
}