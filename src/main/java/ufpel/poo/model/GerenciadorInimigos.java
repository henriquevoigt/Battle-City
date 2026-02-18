package ufpel.poo.model;

import ufpel.poo.controller.MotorFisica; 

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;
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

    public void atualizar(List<Inimigo> inimigosEmCena, Mapa mapa, MotorFisica motor, Dificuldade dif) {

        if (inimigosPendentes <= 0 || inimigosEmCena.size() >= MAX_INIMIGOS_TELA) {
            return;
        }

        long agora = System.currentTimeMillis();
        if (agora - ultimoTempoSpawn < INTERVALO_SPAWN) return;

        for (int tentativa = 0; tentativa < 3; tentativa++) {

            int x = PONTOS_SPAWN_X[random.nextInt(PONTOS_SPAWN_X.length)];
            int y = 40; 

            Rectangle rectFuturo = new Rectangle(x, y, 40, 40);

            if (motor.isPosicaoLivre(rectFuturo, null)) {

                int indexAtual = inimigosPendentes;

                Inimigo novoInimigo = criarInimigo(dif, indexAtual, x, y, mapa, motor);

                inimigosEmCena.add(novoInimigo);

                new Thread(novoInimigo).start();

                inimigosPendentes--;
                ultimoTempoSpawn = agora;
                return; 
            }
        }
    }

    private Inimigo criarInimigo(Dificuldade dif, int index, int x, int y, Mapa mapa, MotorFisica motor) {
         if (dif == Dificuldade.FACIL) {
            if (index == 1) {
                return new InimigoAgil(x, y, mapa, motor);
            }
            return new InimigoDefault(x, y, mapa, motor);
         } 
         else if (dif == Dificuldade.MEDIO) {
            if (index % 2 != 0) {
                return new InimigoAgil(x, y, mapa, motor);
            }
            return new InimigoDefault(x, y, mapa, motor);
         } 
         else { 
            if (index % 3 == 0) {
                return new InimigoBlindado(x, y, mapa, motor);
            }
            else if (index % 3 == 1) {
                return new InimigoAgil(x, y, mapa, motor);
            }
            return new InimigoDefault(x, y, mapa, motor);
         }
    }

    public int getPendentes() {
        return inimigosPendentes;
    }
}