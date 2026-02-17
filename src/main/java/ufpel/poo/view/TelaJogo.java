package ufpel.poo.view;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;      
import java.awt.Rectangle;

import ufpel.poo.model.Direcao;
import ufpel.poo.model.IValidadorMovimento;
import ufpel.poo.model.EntidadeDinamica;
import ufpel.poo.model.GerenciadorInimigos;
import ufpel.poo.model.Mapa;
import ufpel.poo.model.Projetil;
import ufpel.poo.model.RankingManager;
import ufpel.poo.model.Jogador;
import ufpel.poo.model.Inimigo; 
import ufpel.poo.model.ConfiguracaoJogo;
import ufpel.poo.model.Dificuldade;

public class TelaJogo extends JPanel implements ActionListener, IValidadorMovimento {

    private String nomeJogador;
    private ConfiguracaoJogo config;

    private enum EstadoJogo {
        JOGANDO,
        PAUSADO,
        GAME_OVER
    }

    private EstadoJogo estadoAtual = EstadoJogo.JOGANDO;
    public static boolean jogoPausado = false;

    private Mapa mapa;
    private Jogador jogador;
    private List<Inimigo> inimigos;
    private List<Projetil> balas; 

    private boolean cima, baixo, esquerda, direita; 
    private Timer gameLoop;

    private int faseAtual = 1;
    private int qtdInimigosAtual;

    private JButton btnContinuar;
    private JButton btnReiniciar;
    private JButton btnSair;

    // Variáveis de Tempo e Spawn
    private long momentoInicioFase; 
    private long duracaoFaseAtual;
    private long tempoRestanteVisual; 
    private GerenciadorInimigos spawner; // O Gerenciador
    
    // Checkpoint
    private Jogador.Memento estadoInicioFase;

    private final int TAMANHO_MAPA = 520;
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; 
    private final int ALTURA_LOGICA = TAMANHO_MAPA; 

    public TelaJogo(String nomeJogador, ConfiguracaoJogo config) {
        this.config = config;
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null); 
        
        this.nomeJogador = nomeJogador;
        this.mapa = new Mapa();
        this.inimigos = new ArrayList<>();
        this.balas = new ArrayList<>();
        
        // Inicializa o Gerenciador de Inimigos
        this.spawner = new GerenciadorInimigos();

        carregarMapa(config.getIndiceMapa());

        this.jogador = new Jogador(2 * 40, 12 * 40, config.getTipoTanque()); 
        
        // Salva o estado inicial (Vidas e Pontos)
        this.estadoInicioFase = jogador.criarMemento();

        inicializarBotoes();

        definirQuantidadeInicialInimigos();

        // Configura o Spawner com a quantidade total da fase
        spawner.iniciarFase(this.qtdInimigosAtual);

        // Configura o Tempo
        this.duracaoFaseAtual = calcularDuracaoDaFase();
        this.momentoInicioFase = System.currentTimeMillis();

        configurarInputs();

        // GAME LOOP
        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    private void configurarInputs() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();

                if (estadoAtual == EstadoJogo.JOGANDO) {
                    if (codigo == KeyEvent.VK_ESCAPE) alternarPausa();
                    
                    if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = true;
                    if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = true;
                    if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = true;
                    if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = true;

                    if (codigo == KeyEvent.VK_SPACE) {
                        if (jogador.podeAtirar()) { 
                            Projetil p = new Projetil(jogador.getX(), jogador.getY(), jogador.getDirecao(), jogador, mapa);
                            adicionarBala(p);
                        }
                    }
                }
                else if (estadoAtual == EstadoJogo.PAUSADO) {
                    if (codigo == KeyEvent.VK_ESCAPE) alternarPausa(); 
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (estadoAtual == EstadoJogo.JOGANDO) {
                    int codigo = e.getKeyCode();
                    if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = false;
                    if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = false;
                    if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = false;
                    if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = false;
                }
            }
        });
    }

    @Override
    public synchronized boolean isPosicaoLivre(Rectangle rectFuturo, EntidadeDinamica quemSolicitou) {
        if (mapa.temColisao(rectFuturo)) {
            return false;
        }

        if (quemSolicitou != jogador && jogador != null && jogador.estaVivo()) {
            if (rectFuturo.intersects(jogador.getLimites())) {
                return false;
            }
        }

        for (Inimigo inimigo : inimigos) {
            if (inimigo != quemSolicitou && inimigo.estaVivo()) {
                if (rectFuturo.intersects(inimigo.getLimites())) {
                    return false;
                }
            }
        }
        return true; 
    }

    public void carregarMapa(int indiceMapa) {
        if (this.mapa == null) this.mapa = new Mapa();
        this.mapa.carregarMapaDeArquivo("maps.txt", indiceMapa);
    }

    private void definirQuantidadeInicialInimigos() {
        Dificuldade dif = config.getDificuldade();
        if (dif == Dificuldade.FACIL) {
            qtdInimigosAtual = 3;
        }
        else if (dif == Dificuldade.MEDIO) {
            qtdInimigosAtual = 5;
        }
        else if (dif == Dificuldade.DIFICIL) {
            qtdInimigosAtual = 8;
        }
    }

    private void inicializarBotoes() {
        Font fonteBtn = new Font("Arial", Font.BOLD, 14);

        btnContinuar = criarBotao("Continuar", fonteBtn, e -> alternarPausa());
        btnReiniciar = criarBotao("Reiniciar Fase", fonteBtn, e -> reiniciarFase());
        btnSair = criarBotao("Sair para Menu", fonteBtn, e -> sairParaMenuPrincipal());
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

    private long calcularDuracaoDaFase() {
        long base = 60;
        long bonusPorInimigo = qtdInimigosAtual * 10;
        long bonusPorFase = faseAtual * 15;
        return (base + bonusPorInimigo + bonusPorFase) * 1000; 
    }

    private void alternarPausa() {
        if (estadoAtual == EstadoJogo.JOGANDO) {
            estadoAtual = EstadoJogo.PAUSADO;
            jogoPausado = true;
            gameLoop.stop();    
            mostrarBotoes(true);
        } else if (estadoAtual == EstadoJogo.PAUSADO) {
            estadoAtual = EstadoJogo.JOGANDO;
            jogoPausado = false;
            mostrarBotoes(false);
            resetarControles();
            gameLoop.start();
        }
        repaint(); 
    }
    
    private void mostrarBotoes(boolean visivel) {
        if (visivel) {
            int w = 160; int h = 40;
            int cx = getWidth() / 2 - (w / 2);
            int cy = getHeight() / 2 - 60;
            btnContinuar.setBounds(cx, cy, w, h);
            btnReiniciar.setBounds(cx, cy + 50, w, h);
            btnSair.setBounds(cx, cy + 100, w, h);
        }
        btnContinuar.setVisible(visivel);
        btnReiniciar.setVisible(visivel);
        btnSair.setVisible(visivel);
    }

    private void resetarControles() {
        cima = false; baixo = false; esquerda = false; direita = false;
    }

    // --- MÉTODOS REMOVIDOS ---
    // spawnarInimigosDaFase() -> REMOVIDO (Delegado ao Spawner)
    // criarInimigoPorDificuldade() -> REMOVIDO (Delegado ao Spawner)
    // spawnarInimigoTeste() -> REMOVIDO (Delegado ao Spawner)

    private void reiniciarFase() {
        for(Projetil p : balas) p.setAtivo(false);
        balas.clear();

        for(Inimigo i : inimigos) i.setAtivo(false);
        inimigos.clear();

        // 1. Recalcula quantidade total
        definirQuantidadeInicialInimigos(); 
        
        // 2. Reseta o tempo
        this.duracaoFaseAtual = calcularDuracaoDaFase();
        this.momentoInicioFase = System.currentTimeMillis();

        // 3. Recria Jogador e restaura Memento
        this.jogador = new Jogador(2 * 40, 12 * 40, config.getTipoTanque());
        if (this.estadoInicioFase != null) {
            this.jogador.restaurarMemento(this.estadoInicioFase);
        }

        // 4. Carrega Mapa
        int indiceMapaFase = (config.getIndiceMapa() + faseAtual - 1) % 3; // Ajustar se tiver 5 mapas
        carregarMapa(indiceMapaFase); 

        // 5. Reinicia o Spawner
        spawner.iniciarFase(this.qtdInimigosAtual);

        if (estadoAtual == EstadoJogo.PAUSADO) {
            alternarPausa();
        }
    }

    private void sairParaMenuPrincipal() {
        gameLoop.stop(); 
        jogoPausado = false;

        for(Projetil p : balas) p.setAtivo(false);
        for(Inimigo i : inimigos) i.setAtivo(false);

        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof Janela) {
            ((Janela) window).mostrarMenu(); 
        }
    }

    // --- GAME LOOP ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (estadoAtual == EstadoJogo.JOGANDO) {
            atualizarJogo();
            repaint();
        }
    }

    private void atualizarJogo() {
        if (cima) { 
            jogador.setDirecao(Direcao.CIMA); 
            jogador.mover(this);
        }
        else if (baixo) { 
            jogador.setDirecao(Direcao.BAIXO); 
            jogador.mover(this); 
        }
        else if (esquerda) { 
            jogador.setDirecao(Direcao.ESQUERDA); 
            jogador.mover(this); 
        }
        else if (direita) { 
            jogador.setDirecao(Direcao.DIREITA); 
            jogador.mover(this); 
        }

        // --- GERENCIADOR DE INIMIGOS ---
        // O Spawner decide se cria novos inimigos agora
        spawner.atualizar(inimigos, mapa, this, config.getDificuldade());

        // --- TEMPO ---
        long agora = System.currentTimeMillis();
        long tempoDecorrido = agora - momentoInicioFase;
        
        long tempoRestanteMs = duracaoFaseAtual - tempoDecorrido;
        this.tempoRestanteVisual = tempoRestanteMs / 1000;

        if (tempoRestanteMs <= 0) {
            this.tempoRestanteVisual = 0;
            JOptionPane.showMessageDialog(this, "Tempo Esgotado! Vitória por Resistência!");
            avancarParaProximaFase();
            return;
        }

        verificarColisoesGlobais();

        // Limpeza de objetos mortos
        balas.removeIf(p -> !p.isAtivo());
        inimigos.removeIf(i -> !i.estaVivo());

        // --- CONDIÇÃO DE VITÓRIA ATUALIZADA ---
        // Só ganha se não tem inimigos na tela E o estoque do Spawner acabou
        if (inimigos.isEmpty() && spawner.getPendentes() == 0) {
            avancarParaProximaFase();
        }

        // Checa GameOver
        if (mapa.verificarGameOver()) {
            gameOver("BASE DESTRUÍDA!");
        } else {
            verificarMorteJogador();
        }
    }

    public synchronized void adicionarBala(Projetil p) {
        balas.add(p);
    }

    private void gameOver(String motivo) {
        estadoAtual = EstadoJogo.GAME_OVER;
        gameLoop.stop();
        jogoPausado = false; 
        
        RankingManager.salvarPontuacao(nomeJogador, jogador.getPontuacao());
        JOptionPane.showMessageDialog(this, "GAME OVER\n" + motivo + "\nPontuação: " + jogador.getPontuacao());

        java.awt.Window win = SwingUtilities.getWindowAncestor(this);
        if (win != null) win.dispose();
        new Janela(); // Reinicia o app
    }

    private void verificarColisoesGlobais() {
        List<Projetil> listaBalas = new ArrayList<>(balas);

        for (int i = 0; i < listaBalas.size(); i++) {
            Projetil b1 = listaBalas.get(i);
            for (int j = i + 1; j < listaBalas.size(); j++) {
                Projetil b2 = listaBalas.get(j);
                if (b1.isAtivo() && b2.isAtivo()) {
                    if (b1.getLimites().intersects(b2.getLimites())) {
                        if (b1.ehDoJogador() != b2.ehDoJogador()) {
                            b1.setAtivo(false);
                            b2.setAtivo(false);
                        }
                    }
                }
            }
        }

        for (Projetil bala : listaBalas) {
            if (!bala.isAtivo()) continue;
            if (bala.ehDoJogador()) {
                for (Inimigo inimigo : inimigos) {
                    if (inimigo.estaVivo() && bala.getLimites().intersects(inimigo.getLimites())) {
                        inimigo.receberDano(1);
                        bala.setAtivo(false); 
                        if (!inimigo.estaVivo()) {
                            jogador.adicionarPontos(inimigo.getPontuacao());
                        }
                        break;
                    }
                }
            } else {
                if (jogador.estaVivo() && bala.getLimites().intersects(jogador.getLimites())) {
                    jogador.receberDano(1);
                    bala.setAtivo(false);
                }
            }
        }
    }

    public void verificarMorteJogador() {
        if (jogador != null && jogador.getVidas() <= 0) {
            if (!jogador.tentarRespawn()) {
                gameOver("SEM VIDAS!");
            }
        }
    }

    private void avancarParaProximaFase() {
        gameLoop.stop();
        
        resetarControles();

        // Checkpoint: salva o estado atual
        this.estadoInicioFase = jogador.criarMemento();

        faseAtual++;

        // Aumenta dificuldade
        int qtdAntiga = qtdInimigosAtual;
        qtdInimigosAtual = (int) (qtdInimigosAtual * 1.3);
        if (qtdInimigosAtual <= qtdAntiga) {
            qtdInimigosAtual++;
        }

        balas.clear();
        jogador.resetarParaNovaFase();

        this.duracaoFaseAtual = calcularDuracaoDaFase();
        this.momentoInicioFase = System.currentTimeMillis();

        int proximoIndiceMapa = (config.getIndiceMapa() + faseAtual - 1) % 3; // Ajustar se necessário
        carregarMapa(proximoIndiceMapa);

        // --- ATUALIZA SPAWNER PARA NOVA FASE ---
        spawner.iniciarFase(this.qtdInimigosAtual);

        JOptionPane.showMessageDialog(this, "Vitória! Avançando para Fase " + faseAtual + 
                "\nInimigos: " + qtdInimigosAtual);
        
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        java.awt.geom.AffineTransform transformOriginal = g2d.getTransform();

        // ZOOM
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);
        if (escala <= 0) escala = 1;
        
        g2d.translate((getWidth() - LARGURA_LOGICA*escala)/2, (getHeight() - ALTURA_LOGICA*escala)/2);
        g2d.scale(escala, escala);
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);

        if (mapa != null) mapa.desenharFundo(g2d);

        if (jogador != null && jogador.estaVivo()) { 
            jogador.desenhar(g2d);
        }
        try {
            // Cria cópia da lista para evitar ConcurrentModificationException no loop de desenho
            for (Inimigo inimigo : new ArrayList<>(inimigos)) {
                if(inimigo.estaVivo()) inimigo.desenhar(g2d);
            }
            for (Projetil p : new ArrayList<>(balas)) {
                if(p.isAtivo()) p.desenhar(g2d);
            }
        } catch (Exception e) {}

        if (mapa != null) mapa.desenharTopo(g2d);

        desenharHUD(g2d);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(0, 0, LARGURA_LOGICA - 1, ALTURA_LOGICA - 1);

        g2d.setTransform(transformOriginal);

        if (estadoAtual == EstadoJogo.PAUSADO) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String txt = "PAUSADO";
            g.drawString(txt, getWidth()/2 - g.getFontMetrics().stringWidth(txt)/2, getHeight()/2 - 100);
        }
    }

    private void desenharHUD(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        
        int xHud = TAMANHO_MAPA + 20;

        g2d.drawString("TEMPO", xHud, 300);
        if (tempoRestanteVisual <= 10) g2d.setColor(Color.RED);
        else g2d.setColor(Color.BLUE);
        g2d.drawString(String.valueOf(tempoRestanteVisual), xHud, 330);

        g2d.setColor(Color.BLACK);
        
        g2d.drawString("INIMIGOS", xHud, 50);
        
        // --- MOSTRA O TOTAL (Vivos + Reserva) ---
        int totalRestante = inimigos.size() + spawner.getPendentes();
        g2d.drawString("" + totalRestante, xHud, 80);
        
        g2d.drawString("JOGADOR", xHud, 150);
        int vidas = (jogador != null) ? jogador.getEstoqueVidas() + 1 : 0;
        g2d.drawString("Vidas: " + vidas, xHud, 180);
        g2d.drawString("Pontos: " + (jogador != null ? jogador.getPontuacao() : 0), xHud, 210);

        g2d.drawString("FASE " + faseAtual, TAMANHO_MAPA + 20, 400);
    }
}