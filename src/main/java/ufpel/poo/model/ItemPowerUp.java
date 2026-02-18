package ufpel.poo.model;

import ufpel.poo.interfaces.IDesenhavel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ItemPowerUp implements IDesenhavel {

    private int x, y;
    private TipoPowerUp tipo;
    private boolean ativo;
    private long tempoCriacao;
    private final int DURACAO_NO_MAPA = 10000; 
    
    public ItemPowerUp(int x, int y, TipoPowerUp tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.ativo = true;
        this.tempoCriacao = System.currentTimeMillis();
    }

    public void atualizar() {
        if (!ativo) return;

        if (System.currentTimeMillis() - tempoCriacao > DURACAO_NO_MAPA) {
            this.ativo = false;
        }
    }

    public Rectangle getLimites() {
        return new Rectangle(x + 5, y + 5, 30, 30);
    }
    
    public TipoPowerUp getTipo() {
        return tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public void desenhar(Graphics g) {
        if (!ativo) return;
        
        // efeito de piscar quando estiver acabando o tempo
        long tempoVida = System.currentTimeMillis() - tempoCriacao;
        if (tempoVida > DURACAO_NO_MAPA - 2000) { 
            if ((tempoVida / 200) % 2 == 0) return;
        }

        g.setColor(tipo.getCor());
        g.fillRect(x + 5, y + 5, 30, 30);
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y + 5, 30, 30);
        g.setColor(Color.BLACK);
        g.drawString(tipo.name().substring(0, 1), x + 15, y + 25);
    }
}