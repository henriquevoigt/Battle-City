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

import ufpel.poo.model.Direcao;
import ufpel.poo.model.Mapa;
import ufpel.poo.model.Projetil;
import ufpel.poo.model.RankingManager;
import ufpel.poo.model.Jogador;
import ufpel.poo.model.Inimigo; 
import ufpel.poo.model.InimigoAgil;
import ufpel.poo.model.InimigoBlindado;
import ufpel.poo.model.InimigoDefault;

public class TelaJogo extends JPanel implements ActionListener {

    private String nomeJogador;

    private enum EstadoJogo {
        JOGANDO,
        PAUSADO,
        GAME_OVER
    }

    private EstadoJogo estadoAtual = EstadoJogo.JOGANDO;
    
    // flag estática acessada pelos Projéteis/Inimigos threads
    public static boolean jogoPausado = false;

    
    private Mapa mapa;
    private Jogador jogador;
    private List<Inimigo> inimigos;
   
    private List<Projetil> balas; 

    private boolean cima, baixo, esquerda, direita; 
    private Timer gameLoop;

    private JButton btnContinuar;
    private JButton btnReiniciar;
    private JButton btnSair;
 
    private final int TAMANHO_MAPA = 520;
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; 
    private final int ALTURA_LOGICA = TAMANHO_MAPA; 

    public TelaJogo(String nomeJogador) {
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null); 
        
        this.nomeJogador = nomeJogador;
        this.mapa = new Mapa();
        this.inimigos = new ArrayList<>();
        this.balas = new ArrayList<>();

        carregarMapa(0);

        this.jogador = new Jogador(4 * 40, 12 * 40); 

        inicializarBotoes();

        // spawns iniciais
        spawnarInimigoTeste(new InimigoAgil(40, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoDefault(240, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoBlindado(440, 40, this.mapa, this));

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

    public void carregarMapa(int indiceMapa) {
        if (this.mapa == null) this.mapa = new Mapa();
        this.mapa.carregarMapaDeArquivo("maps.txt", indiceMapa);
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

    private void reiniciarFase() {
        for(Projetil p : balas) p.setAtivo(false);
        balas.clear();

        for(Inimigo i : inimigos) i.setAtivo(false);
        inimigos.clear();

        this.jogador = new Jogador(4 * 40, 12 * 40);

        carregarMapa(0); 

        spawnarInimigoTeste(new InimigoAgil(40, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoDefault(240, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoBlindado(440, 40, this.mapa, this));

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

    private void spawnarInimigoTeste(Inimigo inimigo) {
        inimigos.add(inimigo);
        Thread t = new Thread(inimigo);
        t.start();
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
            jogador.setDirecao(Direcao.CIMA); jogador.mover(mapa); 
        }
        else if (baixo) { 
            jogador.setDirecao(Direcao.BAIXO); jogador.mover(mapa); 
        }
        else if (esquerda) { 
            jogador.setDirecao(Direcao.ESQUERDA); jogador.mover(mapa); 
        }
        else if (direita) { 
            jogador.setDirecao(Direcao.DIREITA); jogador.mover(mapa); 
        }

        verificarColisoesGlobais();

        // limpeza de objetos mortos
        balas.removeIf(p -> !p.isAtivo());
        inimigos.removeIf(i -> !i.estaVivo());

        // checa GameOver
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
        jogoPausado = false; // libera threads pra morrerem
        
        RankingManager.salvarPontuacao(nomeJogador, jogador.getPontuacao());
        JOptionPane.showMessageDialog(this, "GAME OVER\n" + motivo + "\nPontuação: " + jogador.getPontuacao());

        java.awt.Window win = SwingUtilities.getWindowAncestor(this);
        if (win != null) win.dispose();
        new Janela();
    }

    private void verificarColisoesGlobais() {
                
        for (Projetil bala : new ArrayList<>(balas)) {
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
            } 

            else {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        java.awt.geom.AffineTransform transformOriginal = g2d.getTransform();

        // ZOOM
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);
        if (escala <= 0) {
            escala = 1;
        }
        g2d.translate((getWidth() - LARGURA_LOGICA*escala)/2, (getHeight() - ALTURA_LOGICA*escala)/2);
        g2d.scale(escala, escala);
        
        // Fundo
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);

        if (mapa != null) mapa.desenharFundo(g2d);

        if (jogador != null && jogador.estaVivo()) { 
            jogador.desenhar(g2d);
        }
        try {
            for (Inimigo inimigo : new ArrayList<>(inimigos)) {
                if(inimigo.estaVivo()) inimigo.desenhar(g2d);
            }
            for (Projetil p : new ArrayList<>(balas)) {
                if(p.isAtivo()) p.desenhar(g2d);
            }
        } catch (Exception e) {}

        if (mapa != null) mapa.desenharTopo(g2d);

        desenharHUD(g2d);

        // bordas
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(0, 0, LARGURA_LOGICA - 1, ALTURA_LOGICA - 1);

        g2d.setTransform(transformOriginal);

        // PAUSE OVERLAY
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
        
        g2d.drawString("INIMIGOS", xHud, 50);
        g2d.drawString("" + inimigos.size(), xHud, 80);
        
        g2d.drawString("JOGADOR", xHud, 150);
        int vidas = (jogador != null) ? jogador.getEstoqueVidas() + 1 : 0;
        g2d.drawString("Vidas: " + vidas, xHud, 180);
        g2d.drawString("Pontos: " + (jogador != null ? jogador.getPontuacao() : 0), xHud, 210);
    }
}