package ufpel.poo.model;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import ufpel.poo.view.TelaJogo;

public class GerenciadorInimigos {

    private int inimigosPendentes;      
    private long ultimoTempoSpawn;
    private final long INTERVALO_SPAWN = 3000; 
    private final int MAX_INIMIGOS_TELA = 4;

    private final int[] PONTOS_SPAWN_X = {0, 6 * 40, 12 * 40};
    
    private Random random;

    public GerenciadorInimigos() {
        this.random = new Random();
    }

    public void iniciarFase(int quantidadeTotal) {
        this.inimigosPendentes = quantidadeTotal;
        this.ultimoTempoSpawn = System.currentTimeMillis();
    }

    public void atualizar(List<Inimigo> inimigosEmCena, Mapa mapa, TelaJogo tela, Dificuldade dif) {

        if (inimigosPendentes <= 0) {
            return;
        }
        if (inimigosEmCena.size() >= MAX_INIMIGOS_TELA) {
            return;
        }

        long agora = System.currentTimeMillis();
        if (agora - ultimoTempoSpawn < INTERVALO_SPAWN) return;

 
        for (int tentativa = 0; tentativa < 3; tentativa++) {

            int x = PONTOS_SPAWN_X[random.nextInt(PONTOS_SPAWN_X.length)];
            int y = 40; 

            Rectangle rectFuturo = new Rectangle(x, y, 40, 40);

            if (isPosicaoLivre(rectFuturo, inimigosEmCena, mapa)) {

                int indexAtual = inimigosPendentes;
                Inimigo novoInimigo = criarInimigo(dif, indexAtual, x, y, mapa, tela);

                inimigosEmCena.add(novoInimigo);

                new Thread(novoInimigo).start();

                inimigosPendentes--;
                ultimoTempoSpawn = agora;
                return; 
            }
        }
    }

    private boolean isPosicaoLivre(Rectangle rect, List<Inimigo> inimigos, Mapa mapa) {
        if (mapa.temColisao(rect)) return false;
        
        for (Inimigo i : inimigos) {
            if (i.estaVivo() && i.getLimites().intersects(rect)) {
                return false;
            }
        }
        return true;
    }

    private Inimigo criarInimigo(Dificuldade dif, int index, int x, int y, Mapa mapa, TelaJogo tela) {
         if (dif == Dificuldade.FACIL) {
            if (index == 1) {
                return new InimigoAgil(x, y, mapa, tela);
            }
            return new InimigoDefault(x, y, mapa, tela);
         } 
         else if (dif == Dificuldade.MEDIO) {
            if (index % 2 != 0) {
                return new InimigoAgil(x, y, mapa, tela);
            }
            return new InimigoDefault(x, y, mapa, tela);
         } 
         else { 
            if (index % 3 == 0) {
                return new InimigoBlindado(x, y, mapa, tela);
            }
            else if (index % 3 == 1) {
                return new InimigoAgil(x, y, mapa, tela);
            }
            return new InimigoDefault(x, y, mapa, tela);
         }
    }

    public int getPendentes() {
        return inimigosPendentes;
    }
}