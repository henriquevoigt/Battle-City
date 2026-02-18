package ufpel.poo.interfaces;

import java.awt.Rectangle;

public interface IMovel {

    public void mover(IValidadorMovimento validador);
        
    public Rectangle getLimites();

    public int getX();
    public int getY();
}