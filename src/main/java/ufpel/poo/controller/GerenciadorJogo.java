package ufpel.poo.controller;

import ufpel.poo.interfaces.IObservadorMapa;
import ufpel.poo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class GerenciadorJogo implements IObservadorMapa {

    private EstadoJogo estadoAtual;
    private String nomeJogador;
    private ConfiguracaoJogo config;
    private Jogador.Memento estadoInicioFase;

    // modelos
    private Mapa mapa;
    private Jogador jogador;
    private List<Inimigo> inimigos;
    private List<Projetil> balas;
    private List<ItemPowerUp> itens;
    
    // controle de tempo e efeitos
    private long fimEfeitoRelogio = 0;
    private long fimEfeitoCapacete = 0;
    private long fimEfeitoEstrela = 0;
    private Mapa.Memento backupBase;
    private long fimEfeitoPa = 0;
    
    private final int DURACAO_POWERUP = 10000;

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
        this.itens = new ArrayList<>();
        this.spawner = new GerenciadorInimigos();
        
        iniciarNovaFase(config.getIndiceMapa());
    }

    private void iniciarNovaFase(int indiceMapa) {
        limparEntidades();
        
        this.mapa = new Mapa();
        this.mapa.setObservador(this);
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

        this.fimEfeitoCapacete = 0;
        this.fimEfeitoEstrela = 0;
        this.fimEfeitoRelogio = 0;
        this.fimEfeitoPa = 0;
        
        this.estadoAtual = EstadoJogo.JOGANDO;
        this.motorFisica.setJogoPausado(false);

        this.estadoInicioFase = jogador.criarMemento();
    }

    public void atualizar() {
        if (estadoAtual != EstadoJogo.JOGANDO) return;

        long agora = System.currentTimeMillis(); 

        if (!jogador.estaVivo()) {
            if (jogador.podeRenascer()) {
                jogador.renascer();

                this.fimEfeitoCapacete = agora + 3000; 
                
                System.out.println("Jogador renasceu! Invulnerável por 3s.");
            } else {
                finalizarJogo("SEM VIDAS!");
                return;
            }
        }

        if (fimEfeitoCapacete > 0) {
            if (agora > fimEfeitoCapacete) { 
                jogador.setInvulneravel(false);
                fimEfeitoCapacete = 0;
            }
        }

        if (fimEfeitoEstrela > 0) {
            if (agora > fimEfeitoEstrela) {
                jogador.resetarTiro();
                fimEfeitoEstrela = 0;
            }
        }

        if (fimEfeitoRelogio > 0) {
            if (agora < fimEfeitoRelogio) { 
                for(Inimigo i : inimigos) i.setCongelado(true);
            } else {
                for(Inimigo i : inimigos) i.setCongelado(false);
                fimEfeitoRelogio = 0;
            }
        }

        if (fimEfeitoPa > 0 && agora > fimEfeitoPa) {
            mapa.restaurarBackupBase(backupBase);
            backupBase = null;
            fimEfeitoPa = 0;
        }

        spawner.atualizar(inimigos, mapa, motorFisica, config.getDificuldade());

        long tempoDecorrido = agora - momentoInicioFase;
        long tempoRestanteMs = duracaoFaseAtual - tempoDecorrido;
        this.tempoRestanteVisual = Math.max(0, tempoRestanteMs / 1000);

        if (tempoRestanteMs <= 0) {
            finalizarFase(true);
            return;
        }

        Rectangle rectJogador = jogador.getLimites();

        for (ItemPowerUp item : itens) {
            item.atualizar();

            if (item.isAtivo() && item.getLimites().intersects(rectJogador)) {
                aplicarEfeitoPowerUp(item.getTipo());
                item.setAtivo(false);
            }
        }

        itens.removeIf(i -> !i.isAtivo());

        if (mapa.verificarGameOver()) {
            finalizarJogo("BASE DESTRUÍDA!");
        } else if (inimigos.isEmpty() && spawner.getPendentes() == 0) {
            finalizarFase(false);
        }

        inimigos.removeIf(i -> !i.estaVivo());
        balas.removeIf(p -> !p.isAtivo());
    }

    public void processarComandoJogador(boolean cima, boolean baixo, boolean esq, boolean dir, boolean atirar) {
        if (estadoAtual != EstadoJogo.JOGANDO) return;

        if (!jogador.estaVivo()) return;

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
            Projetil p = jogador.atirar(motorFisica);
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
            qtdInimigosFase = 8;
        }
        else if (dif == Dificuldade.MEDIO) {
            qtdInimigosFase = 12;
        }
        else {
            qtdInimigosFase = 16;
        }
    }

    private long calcularDuracaoDaFase() {
        return (60 + (qtdInimigosFase * 5) + (faseAtual * 15)) * 1000;
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
        if (inimigos != null) for (Inimigo i : inimigos) i.setAtivo(false);
        if (balas != null) for (Projetil p : balas) p.setAtivo(false);
        try { Thread.sleep(20); } catch (InterruptedException e) {}
        if (inimigos != null) inimigos.clear();
        if (balas != null) balas.clear();
        if (itens != null) itens.clear();
    }

    public void spawnarPowerUp(int x, int y) {
        TipoPowerUp[] tipos = TipoPowerUp.values();
        int indice = (int) (Math.random() * tipos.length);
        ItemPowerUp item = new ItemPowerUp(x, y, tipos[indice]);
        this.itens.add(item);
    }

    @Override
    public void onBlocoDestruido(int x, int y) {
        if (Math.random() < 0.10) { 
            spawnarPowerUp(x, y);
        }
    }

    private void aplicarEfeitoPowerUp(TipoPowerUp tipo) {
        System.out.println("COLETOU POWER-UP: " + tipo);
        
        switch (tipo) {
            case VIDA:
                jogador.ganharVidaExtra();
                break;
                
            case BOMBA:
                for (Inimigo inimigo : inimigos) {
                    if (inimigo.estaVivo()) {
                        jogador.adicionarPontos(inimigo.getPontuacao());
                        inimigo.receberDano(999);
                    }
                }
                break;
                
            case ESTRELA:
                this.fimEfeitoEstrela = System.currentTimeMillis() + DURACAO_POWERUP;
                jogador.melhorarTiro();
                break;
                
            case RELOGIO:
                this.fimEfeitoRelogio = System.currentTimeMillis() + DURACAO_POWERUP;
                for (Inimigo i : inimigos) {
                    i.setCongelado(true);
                }
                break;
                
            case CAPACETE:
                this.fimEfeitoCapacete = System.currentTimeMillis() + DURACAO_POWERUP;
                jogador.setInvulneravel(true);
                break;
                
            case PA:
                if (backupBase == null) {
                    this.backupBase = mapa.criarBackupBase();
                }
                this.fimEfeitoPa = System.currentTimeMillis() + 10000;
                int[][] coords = {{5,11}, {5,12}, {6,11}, {7,11}, {7,12}};
                for (int[] pos : coords) {
                    mapa.setBloco(pos[0], pos[1], BlocoFactory.criar(4, pos[0], pos[1]));
                }
                break;
        }
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
    public List<ItemPowerUp> getItens() { 
        return new ArrayList<>(itens); 
    }
    public boolean isCapaceteAtivo() {
        return fimEfeitoCapacete > 0;
    }
    public boolean isEstrelaAtiva() {
        return fimEfeitoEstrela > 0;
    }
    public boolean isRelogioAtivo() {
        return fimEfeitoRelogio > 0;
    }
    public boolean isPaAtiva() {
        return fimEfeitoPa > 0;
    }
}