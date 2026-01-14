package ufpel.poo;

public abstract class bloco {
    private int id;
    private boolean destrutivel;
    
    public boolean getDestrutivel(){
        return destrutivel;
    }
    public int getId(){
        return id;
    }
    public boolean getTransitavel(){
        return (id == 3 || id == 0);
    }
}
