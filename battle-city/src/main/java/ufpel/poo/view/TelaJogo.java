package ufpel.poo.view;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import ufpel.poo.model.Mapa;
import ufpel.poo.model.Tanque;

public class TelaJogo extends JPanel {
    private Mapa mapa;
    private Tanque jogador;

    // --- CONFIGURAÇÃO DA RESOLUÇÃO ---
    private final int TAMANHO_MAPA = 520; // 13 * 40
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; // 720
    private final int ALTURA_LOGICA = TAMANHO_MAPA; // 520

    public TelaJogo() {

        setBackground(new Color(30, 30, 30));
        
        setFocusable(true);
        
        this.mapa = new Mapa();
        this.jogador = new Tanque(4 * 40, 12 * 40); 
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // --- CÁLCULO DE ZOOM ---
    
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);

        int larguraReal = (int) (LARGURA_LOGICA * escala);
        int alturaReal = (int) (ALTURA_LOGICA * escala);
        int margemX = (getWidth() - larguraReal) / 2;
        int margemY = (getHeight() - alturaReal) / 2;

        // centraliza e aplica o Zoom
        g2d.translate(margemX, margemY);
        g2d.scale(escala, escala);
        
        // area do Mapa
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);

        // area do HUD: 
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);

        // --- ELEMENTOS DO JOGO ---
        if (mapa != null) mapa.desenhar(g2d);
        if (jogador != null) jogador.desenhar(g2d);

        // --- TEXTOS DO HUD ---
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("INIMIGOS", TAMANHO_MAPA + 20, 50);
        g2d.drawString("20", TAMANHO_MAPA + 20, 80);
        g2d.drawString("JOGADOR 1", TAMANHO_MAPA + 20, 150);
        g2d.drawString("Vidas: 3", TAMANHO_MAPA + 20, 180);
        g2d.drawString("FASE 1", TAMANHO_MAPA + 20, 400);

        // --- BORDAS FINAIS ---
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1)); // espessura da linha
        g2d.drawRect(0, 0, LARGURA_LOGICA - 1, ALTURA_LOGICA - 1);
        g2d.drawLine(TAMANHO_MAPA, 0, TAMANHO_MAPA, ALTURA_LOGICA);
    }
}