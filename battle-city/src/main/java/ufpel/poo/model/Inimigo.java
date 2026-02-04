package ufpel.poo.model;

import java.util.Random;

public abstract class Inimigo extends Tanque implements Runnable {

    protected Mapa mapaRef;
    protected Random random;

    public Inimigo(int x, int y, Mapa mapa) {
        super(x, y);
        this.mapaRef = mapa;
        this.random = new Random();
    }

    @Override

    public abstract void run(); // cada subclasse deve subscrever sua propria IA

   
}