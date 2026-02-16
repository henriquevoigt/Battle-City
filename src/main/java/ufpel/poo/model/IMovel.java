package ufpel.poo.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface IMovel {

    public void mover(Mapa mapa);
    
    public void desenhar(Graphics g);
    
    public Rectangle getLimites();

    public int getX();
    public int getY();
}