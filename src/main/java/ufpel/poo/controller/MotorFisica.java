package ufpel.poo.controller;

import ufpel.poo.interfaces.IValidadorMovimento;
import ufpel.poo.model.*;

import java.awt.Rectangle;
import java.util.List;

public class MotorFisica implements IValidadorMovimento {

    private Mapa mapa;
    private Jogador jogador;
    private List<Inimigo> inimigos;
    private List<Projetil> balas;
    private boolean jogoPausado = false;

    public MotorFisica(Mapa mapa, Jogador jogador, List<Inimigo> inimigos, List<Projetil> balas) {
        this.mapa = mapa;
        this.jogador = jogador;
        this.inimigos = inimigos;
        this.balas = balas;
    }

    @Override
    public synchronized boolean isPosicaoLivre(Rectangle rect, EntidadeDinamica quemSolicitou) {
        if (colideComBlocosDoMapa(rect)) {
            return false;
        }

        if (quemSolicitou != jogador && jogador != null && jogador.estaVivo()) {
            if (rect.intersects(jogador.getLimites())) return false;
        }

        for (Inimigo inimigo : inimigos) {
            if (inimigo != quemSolicitou && inimigo.estaVivo()) {
                if (rect.intersects(inimigo.getLimites())) return false;
            }
        }

        return true;
    }

    public boolean isJogoPausado() {
        return jogoPausado;
    }

    public void setJogoPausado(boolean pausado) {
        this.jogoPausado = pausado;
    }

    public synchronized void adicionarProjetil(Projetil p) {
        this.balas.add(p);
    }

    private boolean colideComBlocosDoMapa(Rectangle rect) {
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;

        int colIni = Math.max(0, xMin / 40);
        int colFim = Math.min(12, xMax / 40);
        int linIni = Math.max(0, yMin / 40);
        int linFim = Math.min(12, yMax / 40);

        for (int c = colIni; c <= colFim; c++) {
            for (int l = linIni; l <= linFim; l++) {
                Bloco b = mapa.getBloco(c, l);
                if (b != null && !b.ehTransponivel()) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void processarMovimentoBala(Projetil bala) {
        if (!bala.isAtivo()) return;

        Rectangle rectBala = bala.getLimites();

        if (mapa.processarColisaoProjetil(rectBala, bala.getDano())) {
            bala.setAtivo(false);
            return;
        }

        if (bala.ehDoJogador()) {
            for (Inimigo inimigo : inimigos) {
                if (inimigo.estaVivo() && rectBala.intersects(inimigo.getLimites())) {

                    inimigo.receberDano(bala.getDano());
                    
                    bala.setAtivo(false);
                    if (!inimigo.estaVivo()) {
                        jogador.adicionarPontos(inimigo.getPontuacao());
                    }
                    return;
                }
            }
        } else {
            if (jogador != null && jogador.estaVivo() && rectBala.intersects(jogador.getLimites())) {
                jogador.receberDano(bala.getDano());
                
                bala.setAtivo(false);
            }
        }

        for (Projetil outra : balas) {
            if (outra != bala && outra.isAtivo() && rectBala.intersects(outra.getLimites())) {
                if (bala.ehDoJogador() != outra.ehDoJogador()) {
                    bala.setAtivo(false);
                    outra.setAtivo(false);
                    return;
                }
            }
        }
    }
}