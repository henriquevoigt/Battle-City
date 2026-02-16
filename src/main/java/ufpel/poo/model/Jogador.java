package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;

public class Jogador extends Tanque {
    
    private int estoqueVidas;
    private int pontuacao;    
    private int xInicial, yInicial; 

    public Jogador(int x, int y) {
        super(x, y);
        this.xInicial = x;
        this.yInicial = y;
        this.velocidade = 2;
        setVidas(1);     
        this.estoqueVidas = 2;
        this.pontuacao = 0;
        this.direcao = Direcao.CIMA;
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    public int getPontuacao() {
        return pontuacao;
    }
    
    public int getEstoqueVidas() {
        return estoqueVidas;
    }

    @Override
    public boolean receberDano(int dano) {
        
        setVidas(getVidas() - dano);

        if (getVidas() <= 0) {
            boolean conseguiuRespawnar = tentarRespawn();

            return !conseguiuRespawnar;
        }      
        return false; 
    }

    public boolean tentarRespawn() {
        if (estoqueVidas > 0) {
            estoqueVidas--; 

            setVidas(1); 
            
            this.setAtivo(true);
            this.x = xInicial; 
            this.y = yInicial;
            this.direcao = Direcao.CIMA;
            this.balasAtivas = 0;
            
            return true; 
        }
        
        // acabou o jogo
        this.setAtivo(false);
        return false; 
    }

    @Override
    public void desenhar(Graphics g) {
        if (!estaVivo()) {
            return;
        }

        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 40, 40);
        g.setColor(new Color(200, 200, 0)); 
        g.fillRect(x + 10, y + 10, 20, 20);

        // canhão 
        g.setColor(Color.BLACK);
        if (direcao == Direcao.BAIXO)      g.fillRect(x+18, y+20, 4, 20);
        else if (direcao == Direcao.CIMA)  g.fillRect(x+18, y, 4, 20);
        else if (direcao == Direcao.ESQUERDA) g.fillRect(x, y+18, 20, 4);
        else if (direcao == Direcao.DIREITA)  g.fillRect(x+20, y+18, 20, 4);
        
        // esteiras
        g.fillRect(x + 2, y + 2, 36, 4);
        g.fillRect(x + 2, y + 34, 36, 4);
    }
}