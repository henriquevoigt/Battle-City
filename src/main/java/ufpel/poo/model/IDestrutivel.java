package ufpel.poo.model;

public interface IDestrutivel {
    // retorna true se o objeto foi destruído com o dano
    public boolean receberDano(int dano);
}