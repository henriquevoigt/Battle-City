package ufpel.poo.controller;

import ufpel.poo.model.*;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorJogo {

    private EstadoJogo estadoAtual;
    private String nomeJogador;
    private ConfiguracaoJogo config;
    private Jogador.Memento estadoInicioFase;

    // modelos
    private Mapa mapa;
    private Jogador jogador;
    private List<Inimigo> inimigos;
    private List<Projetil> balas;

    private MotorFisica motorFisica;
    private GerenciadorInimigos spawner;

    // fase e tempo
    private int faseAtual = 1;
    private int qtdInimigosFase;
    private long momentoInicioFase;
    private long duracaoFaseAtual;
    private long tempoRestanteVisual;

    public GerenciadorJogo(String nomeJogador, ConfiguracaoJogo config) {
        this.nomeJogador = nomeJogador;
        this.config = config;
        this.inimigos = new ArrayList<>();
        this.balas = new ArrayList<>();
        this.spawner = new GerenciadorInimigos();
        
        iniciarNovaFase(config.getIndiceMapa());
    }

    private void iniciarNovaFase(int indiceMapa) {

        limparEntidades();
        
        this.mapa = new Mapa();
        this.mapa.carregarMapaDeArquivo("maps.txt", indiceMapa);

        if (this.jogador == null) {
            this.jogador = new Jogador(2 * 40, 12 * 40, config.getTipoTanque());
        } else {
            this.jogador.resetarParaNovaFase();
        }

        this.motorFisica = new MotorFisica(mapa, jogador, inimigos, balas);

        definirQuantidadeInimigos();
        spawner.iniciarFase(this.qtdInimigosFase);

        // tempo
        this.duracaoFaseAtual = calcularDuracaoDaFase();
        this.momentoInicioFase = System.currentTimeMillis();
        
        this.estadoAtual = EstadoJogo.JOGANDO;
        this.motorFisica.setJogoPausado(false);

        this.estadoInicioFase = jogador.criarMemento();
    }

    public void atualizar() {
        if (estadoAtual != EstadoJogo.JOGANDO) return;

        spawner.atualizar(inimigos, mapa, motorFisica, config.getDificuldade());

        long tempoDecorrido = System.currentTimeMillis() - momentoInicioFase;
        long tempoRestanteMs = duracaoFaseAtual - tempoDecorrido;
        this.tempoRestanteVisual = Math.max(0, tempoRestanteMs / 1000);

        if (tempoRestanteMs <= 0) {
            finalizarFase(true);
            return;
        }

        if (mapa.verificarGameOver()) {
            finalizarJogo("BASE DESTRUÍDA!");
        } else if (jogador.getVidas() <= 0 && !jogador.tentarRespawn()) {
            finalizarJogo("SEM VIDAS!");
        } else if (inimigos.isEmpty() && spawner.getPendentes() == 0) {
            finalizarFase(false);
        }

        inimigos.removeIf(i -> !i.estaVivo());
        balas.removeIf(p -> !p.isAtivo());
    }

    public void processarComandoJogador(boolean cima, boolean baixo, boolean esq, boolean dir, boolean atirar) {
        if (estadoAtual != EstadoJogo.JOGANDO) return;

        if (cima) { 
            jogador.setDirecao(Direcao.CIMA); jogador.mover(motorFisica); 
        }
        else if (baixo) { 
            jogador.setDirecao(Direcao.BAIXO); jogador.mover(motorFisica); 
        }
        else if (esq) { 
            jogador.setDirecao(Direcao.ESQUERDA); jogador.mover(motorFisica); 
        }
        else if (dir) { 
            jogador.setDirecao(Direcao.DIREITA); jogador.mover(motorFisica); 
        }

        if (atirar && jogador.podeAtirar()) {
            Projetil p = new Projetil(jogador.getX(), jogador.getY(), jogador.getDirecao(), jogador, motorFisica);
            balas.add(p);
        }
    }

    private void finalizarFase(boolean porTempo) {
        this.estadoAtual = EstadoJogo.VITORIA_FASE;
    }

    public void avancarProximaFase() {
        faseAtual++;
        qtdInimigosFase = (int) (qtdInimigosFase * 1.3);
        int proximoMapa = (config.getIndiceMapa() + faseAtual - 1) % 3;
        iniciarNovaFase(proximoMapa);
    }

    private void finalizarJogo(String motivo) {
        this.estadoAtual = EstadoJogo.GAME_OVER;
        RankingManager.salvarPontuacao(nomeJogador, jogador.getPontuacao());
    }

    private void definirQuantidadeInimigos() {
        Dificuldade dif = config.getDificuldade();
        if (dif == Dificuldade.FACIL) {
            qtdInimigosFase = 3;
        }
        else if (dif == Dificuldade.MEDIO) {
            qtdInimigosFase = 5;
        }
        else {
            qtdInimigosFase = 8;
        }
    }

    private long calcularDuracaoDaFase() {
        return (60 + (qtdInimigosFase * 10) + (faseAtual * 15)) * 1000;
    }

    public void alternarPausa() {
        if (estadoAtual == EstadoJogo.JOGANDO) {
            estadoAtual = EstadoJogo.PAUSADO;
            motorFisica.setJogoPausado(true);
        } else if (estadoAtual == EstadoJogo.PAUSADO) {
            estadoAtual = EstadoJogo.JOGANDO;
            motorFisica.setJogoPausado(false);
        }
    }

    public void reiniciarFaseAtual() {
        if (this.estadoInicioFase != null) {
            this.jogador.restaurarMemento(this.estadoInicioFase);
        }

        int indiceMapa = (config.getIndiceMapa() + faseAtual - 1) % 3;

        iniciarNovaFase(indiceMapa);

        this.estadoAtual = EstadoJogo.JOGANDO;
        this.motorFisica.setJogoPausado(false);
    }

    private void limparEntidades() {

        if (inimigos != null) {
            for (Inimigo i : inimigos) {
                i.setAtivo(false); 
            }
        }
        
        if (balas != null) {
            for (Projetil p : balas) {
                p.setAtivo(false); 
            }
        }

        try { 
            Thread.sleep(20); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (inimigos != null) inimigos.clear();
        if (balas != null) balas.clear();
    }

    // getters
    public Mapa getMapa() { 
        return mapa; 
    }
    public Jogador getJogador() { 
        return jogador; 
    }
    public List<Inimigo> getInimigos() { 
        return new ArrayList<>(inimigos); 
    }
    public List<Projetil> getBalas() { 
        return new ArrayList<>(balas); 
    }
    public long getTempoRestante() { 
        return tempoRestanteVisual; 
    }
    public int getFaseAtual() { 
        return faseAtual; 
    }
    public int getInimigosRestantes() { 
        return inimigos.size() + spawner.getPendentes(); 
    }
    public EstadoJogo getEstadoAtual() { 
        return estadoAtual; 
    }
    public String getNomeJogador() { 
        return nomeJogador; 
    }
    public boolean isPausado() {
        return estadoAtual == EstadoJogo.PAUSADO;
    }
}