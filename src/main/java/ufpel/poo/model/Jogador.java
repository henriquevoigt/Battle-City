package ufpel.poo.model;

import java.awt.Color;
import java.awt.Graphics;
import ufpel.poo.controller.MotorFisica;

public class Jogador extends Tanque {
    
    private int estoqueVidas;
    private int pontuacao;    
    private int xInicial, yInicial; 
    private TipoTanque tipo;
    private boolean invulneravel = false; 
    private int nivelDano = 1;        

    public Jogador(int x, int y, TipoTanque tipo) {
        super(x, y);
        this.xInicial = x;
        this.yInicial = y;   
        this.pontuacao = 0;
        this.direcao = Direcao.CIMA;
        this.tipo = tipo;

        if (this.tipo == null) {
            this.tipo = TipoTanque.BALANCEADO;
        }

        switch (this.tipo) {
            case AGIL:
                this.estoqueVidas = 0;
                break;
            case BLINDADO:
                this.estoqueVidas = 1; 
                break;
            case BALANCEADO:
            default:
                this.estoqueVidas = 2; 
                break;
        }

        configurarAtributos(tipo);
    }
    
    // --- MEMENTO ---
    public static class Memento {
        private final int vidas;
        private final int pontuacao;

        public Memento(int vidas, int pontuacao) {
            this.vidas = vidas;
            this.pontuacao = pontuacao;
        }
    }

    private void configurarAtributos(TipoTanque tipo) {
        switch (tipo) {
            case AGIL:
                this.velocidade = 3;
                setVidas(1);   
                break;
            case BLINDADO:
                this.velocidade = 1; 
                setVidas(2);         
                break;
            case BALANCEADO:
            default:
                this.velocidade = 2; 
                setVidas(1);
                break;
        }
    }

    // --- LÓGICA DE TIRO ---
    public Projetil atirar(MotorFisica motor) {
        return new Projetil(x, y, direcao, this, motor, this.nivelDano);
    }

    public void melhorarTiro() {
        this.nivelDano = 2;
    }

    public void resetarTiro() {
        this.nivelDano = 1;
    }

    public void setInvulneravel(boolean invulneravel) {
        this.invulneravel = invulneravel;
    }

    public boolean isInvulneravel() {
        return invulneravel;
    }

    public void ganharVidaExtra() {
        this.estoqueVidas++;
    }

    // --- DANO E RESPAWN ---

    @Override
    public boolean receberDano(int dano) {
        if (this.invulneravel) {
            return false; 
        }

        setVidas(getVidas() - dano);

        if (getVidas() <= 0) {
            this.setAtivo(false);
            return true; 
        }      
        return false; 
    }

    public void renascer() {
        if (estoqueVidas > 0) {
            estoqueVidas--; 

            configurarAtributos(this.tipo); 
            
            this.setAtivo(true);
            this.x = xInicial; 
            this.y = yInicial;
            this.direcao = Direcao.CIMA;
            this.balasAtivas = 0;

            this.nivelDano = 1; 

            this.invulneravel = true;
        }
    }
    
    public boolean podeRenascer() {
        return estoqueVidas > 0;
    }

    public void resetarParaNovaFase() {
        this.x = xInicial;
        this.y = yInicial;
        this.direcao = Direcao.CIMA;
        this.balasAtivas = 0;
        this.setAtivo(true);
    }

    // --- GETTERS E MEMENTO ---

    public Memento criarMemento() {
        return new Memento(this.estoqueVidas, this.pontuacao);
    }

    public void restaurarMemento(Memento m) {
        this.estoqueVidas = m.vidas;
        this.pontuacao = m.pontuacao;
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

    // --- DESENHO ---

    @Override
    public void desenhar(Graphics g) {
        if (!estaVivo()) {
            return;
        }

        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 40, 40);
        g.setColor(new Color(200, 200, 0)); 
        g.fillRect(x + 10, y + 10, 20, 20);

        if (invulneravel) {
            g.setColor(new Color(0, 255, 0, 100));
            g.fillOval(x - 5, y - 5, 50, 50);
            g.setColor(Color.GREEN);
            g.drawOval(x - 5, y - 5, 50, 50);
        }

        // canhao
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