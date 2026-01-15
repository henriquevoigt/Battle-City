package ufpel.poo.view;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import ufpel.poo.model.Direcao;
import ufpel.poo.model.Mapa;
import ufpel.poo.model.Tanque;

public class TelaJogo extends JPanel implements ActionListener {

    private Mapa mapa;
    private Tanque jogador;

    private boolean cima, baixo, esquerda, direita; // "interruptores" de movimento

    private Timer gameLoop;

    // resolução
    private final int TAMANHO_MAPA = 520;
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; // 720px
    private final int ALTURA_LOGICA = TAMANHO_MAPA; // 520px

    public TelaJogo() {

        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        requestFocusInWindow();
        
        this.mapa = new Mapa();
        this.jogador = new Tanque(4 * 40, 12 * 40); 

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();
                // ao apertar a tecla, "liga" o interruptor
                if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = true;
                if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = true;
                if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = true;
                if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int codigo = e.getKeyCode();
                // ao soltar a tecla, "desliga" o interruptor
                if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = false;
                if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = false;
                if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = false;
                if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = false;
            }
        });

        // GAME LOOP = 16ms ~60 FPS
        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atualizarJogo();
        repaint();
    }

    private void atualizarJogo() {
        // verifica qual interruptor ta ligado e move
        if (cima) {
            jogador.setDirecao(Direcao.CIMA);
            jogador.mover();
        } else if (baixo) {
            jogador.setDirecao(Direcao.BAIXO);
            jogador.mover();
        } else if (esquerda) {
            jogador.setDirecao(Direcao.ESQUERDA);
            jogador.mover();
        } else if (direita) {
            jogador.setDirecao(Direcao.DIREITA);
            jogador.mover();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // calcula o zoom
    
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);

        int larguraReal = (int) (LARGURA_LOGICA * escala);
        int alturaReal = (int) (ALTURA_LOGICA * escala);
        int margemX = (getWidth() - larguraReal) / 2;
        int margemY = (getHeight() - alturaReal) / 2;

        // centraliza e aplica o zoom
        g2d.translate(margemX, margemY);
        g2d.scale(escala, escala);
        
        // area do mapa
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);

        // area do HUD: 
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);

        // elementos do jogo
        if (mapa != null) mapa.desenhar(g2d);
        if (jogador != null) jogador.desenhar(g2d);

        // textos do hud
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("INIMIGOS", TAMANHO_MAPA + 20, 50);
        g2d.drawString("20", TAMANHO_MAPA + 20, 80);
        g2d.drawString("JOGADOR 1", TAMANHO_MAPA + 20, 150);
        g2d.drawString("Vidas: 3", TAMANHO_MAPA + 20, 180);
        g2d.drawString("FASE 1", TAMANHO_MAPA + 20, 400);

        // bordas
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1)); // espessura da linha
        g2d.drawRect(0, 0, LARGURA_LOGICA - 1, ALTURA_LOGICA - 1);
        g2d.drawLine(TAMANHO_MAPA, 0, TAMANHO_MAPA, ALTURA_LOGICA);
    }
}