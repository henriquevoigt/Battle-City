package ufpel.poo.model;

import java.awt.Graphics;
import java.awt.Rectangle;
public abstract class Tanque extends EntidadeDinamica {
    
    protected int vidas;
    
    public Tanque(int x, int y) {
        super(x, y);
        // default, os filhos vão sobrescrever
        this.velocidade = 0; 
        this.vidas = 1;
    }

    public Projetil atirar() {
        return new Projetil(this.x, this.y, this.direcao);
    }
    
    public void receberDano() {
        this.vidas--;
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

    public abstract void desenhar(Graphics g); // player é diferente de bot

}