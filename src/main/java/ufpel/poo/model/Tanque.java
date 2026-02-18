package ufpel.poo.model;

import ufpel.poo.interfaces.IDestrutivel; 

import java.awt.Graphics;
public abstract class Tanque extends EntidadeDinamica implements IDestrutivel {

    private int vidas; 

    protected boolean ativo; 
    protected int balasAtivas; 
    protected long ultimoDisparo = 0;
    protected int delayDisparo = 400;

    public Tanque(int x, int y) {
        super(x, y);
        this.ativo = true;
        this.balasAtivas = 0;
        this.vidas = 1;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
        if (this.vidas <= 0) {
            this.ativo = false;
        }
    }

    public boolean estaVivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean podeAtirar() {
        long agora = System.currentTimeMillis();
        return balasAtivas < 1 && (agora - ultimoDisparo > delayDisparo);
    }

    public void registrarDisparo() {
        balasAtivas++;
        this.ultimoDisparo = System.currentTimeMillis();
    }

    public void recarregar() {
        if (balasAtivas > 0) {
            balasAtivas--;
        }
    }
    
    @Override
    public boolean receberDano(int dano) {
        setVidas(getVidas() - dano);
        // retorna true se o tanque morreu (ativo == false)
        return !ativo;
    }

    @Override
    public abstract void desenhar(Graphics g);
}