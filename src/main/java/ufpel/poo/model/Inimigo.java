package ufpel.poo.model;

import ufpel.poo.view.TelaJogo;
import java.awt.Graphics;

public abstract class Inimigo extends Tanque implements Runnable {

    protected Mapa mapa;
    protected TelaJogo tela; 

    public Inimigo(int x, int y, Mapa mapa, TelaJogo tela) {
        super(x, y);
        this.mapa = mapa;
        this.tela = tela; 
    }

    @Override
    public abstract void run();
    
    @Override
    public abstract void desenhar(Graphics g);
}