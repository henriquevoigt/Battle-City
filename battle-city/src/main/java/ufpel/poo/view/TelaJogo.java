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
import java.util.Iterator;  
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

    // --- ESTADOS DO JOGO ---
    private enum EstadoJogo {
        JOGANDO,
        PAUSADO,
        GAME_OVER
    }

    private EstadoJogo estadoAtual = EstadoJogo.JOGANDO;
    
    // flag p/ threads dos inimigos saberem que devem parar
    public static boolean jogoPausado = false;

    // --- OBJETOS DO JOGO ---
    private Mapa mapa;
    private Jogador jogador;
    private int vidasJogador = 3;
    private List<Inimigo> inimigos;
    private List<Projetil> balas;

    // --- CONTROLE ---
    private boolean cima, baixo, esquerda, direita; // flags de movimento
    private Timer gameLoop;

    // --- INTERFACE ---
    private JButton btnContinuar;
    private JButton btnReiniciar;
    private JButton btnSair;
   
    // --- CONSTANTES DE TELA ---
    private final int TAMANHO_MAPA = 520;
    private final int LARGURA_HUD = 200;
    private final int LARGURA_LOGICA = TAMANHO_MAPA + LARGURA_HUD; // 720px
    private final int ALTURA_LOGICA = TAMANHO_MAPA; // 520px

    public TelaJogo(String nomeJogador) {
        // config do painel
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null); // layout NULO para posicionar botoes manualmente
        
        // inicializa objetos
        this.nomeJogador = nomeJogador;
        this.mapa = new Mapa();
        this.jogador = new Jogador(4 * 40, 12 * 40); 
        this.inimigos = new ArrayList<>();
        this.balas = new ArrayList<>();

        // inicializa botoes do menu de pause
        inicializarBotoes();

        // spawn inicial de teste
        spawnarInimigoTeste(new InimigoAgil(40, 40, this.mapa));
        spawnarInimigoTeste(new InimigoDefault(240, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoBlindado(440, 40, this.mapa));

        // inputs
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();

                if (estadoAtual == EstadoJogo.JOGANDO) {
                    if (codigo == KeyEvent.VK_ESCAPE) {
                        alternarPausa();
                    }
                    
                    // movimentaçao
                    if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = true;
                    if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = true;
                    if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = true;
                    if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = true;

                    // tiro
                    if (codigo == KeyEvent.VK_SPACE) {
                        if (balas.size() < 1) { // limite de tiro por tank
                            balas.add(jogador.atirar());
                        }
                    }
                }
                // quando ta PAUSADO
                else if (estadoAtual == EstadoJogo.PAUSADO) {
                    if (codigo == KeyEvent.VK_ESCAPE) {
                        alternarPausa(); // resume 
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // desliga as flags se estiver jogando (evita bugs ao pausar andando)
                if (estadoAtual == EstadoJogo.JOGANDO) {
                    int codigo = e.getKeyCode();
                    if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = false;
                    if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = false;
                    if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = false;
                    if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = false;
                }
            }
        });

        // GAME LOOP = 16ms (~60 FPS)
        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    public void carregarMapa(int indiceMapa) {

        if (this.mapa == null) {
            this.mapa = new Mapa();
        }
        this.mapa.carregarMapaDeArquivo("maps.txt", indiceMapa);
        
    }

    private void inicializarBotoes() {
        Font fonteBtn = new Font("Arial", Font.BOLD, 14);

        // CONTINUAR
        btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(fonteBtn);
        btnContinuar.setFocusable(false);
        btnContinuar.setVisible(false); 
        btnContinuar.addActionListener(e -> alternarPausa());
        add(btnContinuar);

        // REINICIAR
        btnReiniciar = new JButton("Reiniciar Fase");
        btnReiniciar.setFont(fonteBtn);
        btnReiniciar.setFocusable(false);
        btnReiniciar.setVisible(false);
        btnReiniciar.addActionListener(e -> reiniciarFase());
        add(btnReiniciar);

        // SAIR
        btnSair = new JButton("Sair para Menu");
        btnSair.setFont(fonteBtn);
        btnSair.setFocusable(false);
        btnSair.setVisible(false);
        btnSair.addActionListener(e -> sairParaMenuPrincipal());
        add(btnSair);
    }

    // --- LOGICA DE PAUSA ---
    private void alternarPausa() {
        if (estadoAtual == EstadoJogo.JOGANDO) {
            
            estadoAtual = EstadoJogo.PAUSADO;
            jogoPausado = true; 
            gameLoop.stop();    

            // posiciona os botoes do pause
            int w = 160;
            int h = 40;
            int cx = getWidth() / 2 - (w / 2);
            int cy = getHeight() / 2 - 60;

            btnContinuar.setBounds(cx, cy, w, h);
            btnReiniciar.setBounds(cx, cy + 50, w, h);
            btnSair.setBounds(cx, cy + 100, w, h);

            btnContinuar.setVisible(true);
            btnReiniciar.setVisible(true);
            btnSair.setVisible(true);

        } else if (estadoAtual == EstadoJogo.PAUSADO) {
            
            estadoAtual = EstadoJogo.JOGANDO;
            jogoPausado = false;
            
            
            cima = false;       // ]
            baixo = false;      // ] === reseta as flags de movimento
            esquerda = false;   // ]
            direita = false;    // ]
            
            
            btnContinuar.setVisible(false);
            btnReiniciar.setVisible(false);
            btnSair.setVisible(false);
            
            synchronized(this) {
                notifyAll();
            }
            
            gameLoop.start();
        }
        repaint(); 
    }

    private void reiniciarFase() {
        // limpa tudo
        this.inimigos.clear();
        this.balas.clear();
        this.jogador = new Jogador(4 * 40, 12 * 40);
        this.vidasJogador = 3;
        
        // respawna inimigos
        spawnarInimigoTeste(new InimigoAgil(40, 40, this.mapa));
        spawnarInimigoTeste(new InimigoDefault(240, 40, this.mapa, this));
        spawnarInimigoTeste(new InimigoBlindado(440, 40, this.mapa));

        // se pausado, despausa para voltar a jogar
        if (estadoAtual == EstadoJogo.PAUSADO) {
            alternarPausa();
        }
    }

    private void sairParaMenuPrincipal() {
        
        gameLoop.stop(); 
        
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        
        if (window instanceof Janela) {
            Janela framePrincipal = (Janela) window;
            
            framePrincipal.mostrarMenu(); 
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
        // movimento do jogador
        if (cima) {
            jogador.setDirecao(Direcao.CIMA);
            jogador.mover(mapa);
        } else if (baixo) {
            jogador.setDirecao(Direcao.BAIXO);
            jogador.mover(mapa);
        } else if (esquerda) {
            jogador.setDirecao(Direcao.ESQUERDA);
            jogador.mover(mapa);
        } else if (direita) {
            jogador.setDirecao(Direcao.DIREITA);
            jogador.mover(mapa);
        }

        // movimento e colisao dos projeteis
        Iterator<Projetil> itBalas = balas.iterator();
        while (itBalas.hasNext()) {
            Projetil p = itBalas.next();
            p.moverBala(mapa);
            
            // verifica se bateu em inimigos ou no jogador
            verificarColisoes(p);

            // remove se bateu em parede ou explodiu alvo
            if (!p.isAtivo()) {
                itBalas.remove();
            }
        }
        
        // remove inimigos mortos da lista
        Iterator<Inimigo> itInimigos = inimigos.iterator();
        while (itInimigos.hasNext()) {
            Inimigo inimigo = itInimigos.next();
            if (!inimigo.estaVivo()) {
                itInimigos.remove();
                // thread do inimigo encerra sozinha pois o while(estaVivo) no run() vai falhar
            }
        }

        verificarMorteJogador();

    }

    public void adicionarBala(Projetil p) {
        balas.add(p);
    }


    private void gameOver() {
        gameLoop.stop();

        RankingManager.salvarPontuacao(nomeJogador, jogador.getPontuacao());

        JOptionPane.showMessageDialog(this,"GAME OVER\nPontuação: " + jogador.getPontuacao());

        SwingUtilities.getWindowAncestor(this).dispose();
        new Janela();
        System.out.println("GAME OVER CHAMADO");
        System.out.println("Pontuação final: " + jogador.getPontuacao());
    }


    private void verificarColisoes(Projetil bala) {

        if (!bala.isAtivo()) return;

        //  Bala do jogador acerta inimigos
        if (bala.ehDoJogador()) {
            for (Inimigo inimigo : inimigos) {
                if (inimigo.estaVivo() && bala.getLimites().intersects(inimigo.getLimites())) {
                    inimigo.receberDano();
                    bala.setAtivo(false);

                    if (!inimigo.estaVivo()) {
                        jogador.adicionarPontos(100);
                    }
                    return;
                }
            }
        }
        //  Bala do inimigo acerta jogador
        else {
            if (jogador != null && jogador.estaVivo() &&
                bala.getLimites().intersects(jogador.getLimites())) {

                jogador.receberDano(); // jogador perde vida
                bala.setAtivo(false);
            }
        }
    }


    public void verificarMorteJogador() {
        if (jogador != null && jogador.getVidas() <= 0) {
            vidasJogador--;

        if (vidasJogador > 0) {
            int pontosAtuais = jogador.getPontuacao(); // guarda pontos

            jogador = new Jogador(4 * 40, 12 * 40); // cria novo tanque

            jogador.setPontuacao(pontosAtuais); // devolve os pontos
        } else {
            gameOver();
          }   
        }
    }

    // --- DESENHO ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // limpa tela e desenha os botoes
        Graphics2D g2d = (Graphics2D) g;

        java.awt.geom.AffineTransform transformOriginal = g2d.getTransform();

        // ajus o zoom
        double escalaX = (double) getWidth() / LARGURA_LOGICA;
        double escalaY = (double) getHeight() / ALTURA_LOGICA;
        double escala = Math.min(escalaX, escalaY);

        int larguraReal = (int) (LARGURA_LOGICA * escala);
        int alturaReal = (int) (ALTURA_LOGICA * escala);
        int margemX = (getWidth() - larguraReal) / 2;
        int margemY = (getHeight() - alturaReal) / 2;

        g2d.translate(margemX, margemY);
        g2d.scale(escala, escala);
        
        // --- DESENHA ELEMENTOS DO JOGO (COM ZOOM) ---
        
        // fundo do mapa
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, TAMANHO_MAPA, ALTURA_LOGICA);

        // fundo HUD
        g2d.setColor(Color.GRAY);
        g2d.fillRect(TAMANHO_MAPA, 0, LARGURA_HUD, ALTURA_LOGICA);

        // jogador
        if (mapa != null) mapa.desenhar(g2d);
        if (jogador != null && jogador.estaVivo()) jogador.desenhar(g2d);

        // inimigos (proteçao contra concorrencia simples)
        try {
            for (Inimigo inimigo : inimigos) {
                if(inimigo.estaVivo()) {
                    inimigo.desenhar(g2d);
                }
            }
        } catch (Exception e) {  } // ignora erro de lista modificada durante desenho

        for (Projetil p : balas) {
            p.desenhar(g2d);
        }

        // textos do HUD
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("INIMIGOS", TAMANHO_MAPA + 20, 50);
        g2d.drawString("" + inimigos.size(), TAMANHO_MAPA + 20, 80);
        
        g2d.drawString("JOGADOR 1", TAMANHO_MAPA + 20, 150);
        g2d.drawString("Vidas: " + vidasJogador, TAMANHO_MAPA + 20, 180);
        g2d.drawString("Pontos: " + jogador.getPontuacao(), TAMANHO_MAPA + 20, 210);

        g2d.drawString("FASE 1", TAMANHO_MAPA + 20, 400);

        // bordas
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(0, 0, LARGURA_LOGICA - 1, ALTURA_LOGICA - 1);
        g2d.drawLine(TAMANHO_MAPA, 0, TAMANHO_MAPA, ALTURA_LOGICA);

        // remove zoom pro overlay do pause
        g2d.setTransform(transformOriginal);

        // --- OVERLAY DO PAUSE ---
        if (estadoAtual == EstadoJogo.PAUSADO) {
            // fundo escuro 
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // titulo
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String txt = "PAUSADO";
            int txtW = g.getFontMetrics().stringWidth(txt);
            g.drawString(txt, getWidth()/2 - txtW/2, getHeight()/2 - 100);
            
        }
    }
}