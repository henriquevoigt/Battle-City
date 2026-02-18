package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import ufpel.poo.controller.GerenciadorJogo;
import ufpel.poo.model.*;
import ufpel.poo.controller.TecladoAdapter;

public class TelaJogo extends JPanel implements ActionListener {

    private GerenciadorJogo controller;

    private TecladoAdapter input;

    private Timer renderLoop;
    
    // UI
    private JButton btnContinuar, btnReiniciar, btnSair;

    // constantes visuais
    private final int TAMANHO_MAPA = 520;
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; 
    private final int ALTURA_LOGICA = TAMANHO_MAPA; 

    public TelaJogo(String nomeJogador, ConfiguracaoJogo config) {
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null); 

        this.controller = new GerenciadorJogo(nomeJogador, config);

        inicializarBotoes();

        this.input = new TecladoAdapter(controller, this);
        addKeyListener(input);

        renderLoop = new Timer(16, this); // 16ms ≃ 60FPS
        renderLoop.start();
    }

    // --- GAME LOOP VISUAL ---
    @Override
    public void actionPerformed(ActionEvent e) {

        boolean cima = input.isCima();
        boolean baixo = input.isBaixo();
        boolean esq = input.isEsquerda();
        boolean dir = input.isDireita();
        boolean tiro = input.isAtirar();

        controller.processarComandoJogador(cima, baixo, esq, dir, tiro);

        controller.atualizar();

        verificarEstadosCriticos();

        repaint();
    }

    private void verificarEstadosCriticos() {
        if (controller.getEstadoAtual() == EstadoJogo.GAME_OVER) {
            renderLoop.stop();
            JOptionPane.showMessageDialog(this, "Fim de Jogo!");
            sairParaMenu();
        } 
        else if (controller.getEstadoAtual() == EstadoJogo.VITORIA_FASE) {
            renderLoop.stop();
            JOptionPane.showMessageDialog(this, "Fase Concluída! Próxima Fase...");
            
            input.resetarInputs();
            
            controller.avancarProximaFase(); 
            renderLoop.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;

        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        // aplica zoom e desenha o jogo + HUD
        aplicarTransformacao(g2d);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);
        
        Mapa mapa = controller.getMapa();
        if (mapa != null) mapa.desenharFundo(g2d);

        Jogador jogador = controller.getJogador();
        if (jogador != null && jogador.estaVivo()) jogador.desenhar(g2d);

        List<Inimigo> inimigos = controller.getInimigos();
        for (Inimigo i : inimigos) i.desenhar(g2d);

        List<Projetil> balas = controller.getBalas();
        for (Projetil p : balas) p.desenhar(g2d);

        List<ItemPowerUp> itens = controller.getItens();
        for (ItemPowerUp item : itens) {
            item.desenhar(g2d);
        }

        if (mapa != null) mapa.desenharTopo(g2d);

        desenharHUD(g2d);
        desenharOverlayPause(g2d);

        g2d.setTransform(oldTransform);
    }

    private void desenharHUD(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        int xHud = TAMANHO_MAPA + 20;

        g2d.drawString("TEMPO", xHud, 300);
        long tempo = controller.getTempoRestante();
        g2d.setColor(tempo <= 10 ? Color.RED : Color.BLUE);
        g2d.drawString(String.valueOf(tempo), xHud, 330);

        g2d.setColor(Color.BLACK);
        g2d.drawString("INIMIGOS: " + controller.getInimigosRestantes(), xHud, 80);
        
        if (controller.getJogador() != null) {
            g2d.drawString("Vidas: " + (controller.getJogador().getEstoqueVidas() + 1), xHud, 180);
            g2d.drawString("Pontos: " + controller.getJogador().getPontuacao(), xHud, 210);
        }
        
        g2d.drawString("FASE " + controller.getFaseAtual(), xHud, 400);
    }
    
    private void desenharOverlayPause(Graphics2D g) {
        if (controller.isPausado()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, LARGURA_LOGICA, ALTURA_LOGICA);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40)); 
            
            String texto = "PAUSADO";
            FontMetrics metrics = g.getFontMetrics(g.getFont());

            int x = (LARGURA_LOGICA - metrics.stringWidth(texto)) / 2;

            int y = (ALTURA_LOGICA / 2) - 150;
            
            g.drawString(texto, x, y);
        }
    }

    private void aplicarTransformacao(Graphics2D g2d) {
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);
        if (escala <= 0) escala = 1;
        g2d.translate((getWidth() - LARGURA_LOGICA*escala)/2, (getHeight() - ALTURA_LOGICA*escala)/2);
        g2d.scale(escala, escala);
    }

    // --- UI E BOTOES ---
    private void inicializarBotoes() {
        Font fonteBtn = new Font("Arial", Font.BOLD, 14);
        
        btnContinuar = criarBotao("Continuar", fonteBtn, e -> {
            controller.alternarPausa();
            atualizarVisibilidadeBotoes();
            requestFocusInWindow();
        });
        
        btnReiniciar = criarBotao("Reiniciar Fase", fonteBtn, e -> {
            controller.reiniciarFaseAtual();
            atualizarVisibilidadeBotoes();
            requestFocusInWindow();
        });
        
        btnSair = criarBotao("Sair", fonteBtn, e -> sairParaMenu());
    }

    private JButton criarBotao(String texto, Font fonte, ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setFont(fonte);
        btn.setFocusable(false);
        btn.setVisible(false);
        btn.addActionListener(acao);
        add(btn);
        return btn;
    }

    // publico pra ser acessado pelo TecladoAdapter
    public void atualizarVisibilidadeBotoes() {
        boolean visivel = controller.isPausado();
        int w = 160, h = 40;
        int cx = getWidth() / 2 - (w / 2);
        int cy = getHeight() / 2 - 60; 

        if (visivel) {
            btnContinuar.setBounds(cx, cy, w, h);
            btnReiniciar.setBounds(cx, cy + 50, w, h);
            btnSair.setBounds(cx, cy + 100, w, h);
        }
        btnContinuar.setVisible(visivel);
        btnReiniciar.setVisible(visivel);
        btnSair.setVisible(visivel);
    }

    private void sairParaMenu() {
        renderLoop.stop(); 
        java.awt.Window win = SwingUtilities.getWindowAncestor(this);
        if (win instanceof Janela) {
            ((Janela) win).mostrarMenu(); 
        }
    }
}