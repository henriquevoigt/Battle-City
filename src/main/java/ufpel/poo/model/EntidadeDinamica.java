package ufpel.poo.model;

import ufpel.poo.interfaces.IDesenhavel;
import ufpel.poo.interfaces.IMovel;
import ufpel.poo.interfaces.IValidadorMovimento;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class EntidadeDinamica implements IMovel, IDesenhavel {
    protected int x, y;
    protected int velocidade;
    protected Direcao direcao;

    public EntidadeDinamica(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocidade = 4;
        this.direcao = Direcao.CIMA;
    }

    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
    public Direcao getDirecao() { 
        return direcao; 
    }
    
    public void setDirecao(Direcao novaDirecao) {

        if (this.direcao == novaDirecao) {
            return;
        }

        if (novaDirecao == Direcao.ESQUERDA || novaDirecao == Direcao.DIREITA) {
            this.y = arredondarParaGrid(this.y);
        } 
        
        else if (novaDirecao == Direcao.CIMA || novaDirecao == Direcao.BAIXO) {
            this.x = arredondarParaGrid(this.x);
        }

        this.direcao = novaDirecao;
    }

    private int arredondarParaGrid(int valor) {
        int tamanhoBloco = 40;
        int resto = valor % tamanhoBloco;

        if (resto < 10) {
            return valor - resto; 
        } else if (resto > (tamanhoBloco - 10)) {
            return valor + (tamanhoBloco - resto); 
        }
        return valor;
    }

    public void mover(IValidadorMovimento validador) {
        
        int novoX = this.x;
        int novoY = this.y;

        switch (this.direcao) {
            case CIMA:    novoY -= velocidade; break;
            case BAIXO:   novoY += velocidade; break;
            case ESQUERDA: novoX -= velocidade; break;
            case DIREITA:  novoX += velocidade; break;
        }

        if (novoX < 0 || novoX > (520 - 40)) return; 
        if (novoY < 0 || novoY > (520 - 40)) return;

        Rectangle rectFuturo = new Rectangle(novoX + 2, novoY + 2, 36, 36);

        if (validador.isPosicaoLivre(rectFuturo, this)) {
            this.x = novoX;
            this.y = novoY;
        }
    }

    public Rectangle getLimites() {
        return new Rectangle(x + 2, y + 2, 36, 36);
    }

    public abstract void desenhar(Graphics g);
}