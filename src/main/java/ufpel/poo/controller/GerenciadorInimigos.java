package ufpel.poo.controller;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import ufpel.poo.model.Dificuldade;
import ufpel.poo.model.Inimigo;
import ufpel.poo.model.InimigoAgil;
import ufpel.poo.model.InimigoBlindado;
import ufpel.poo.model.InimigoDefault;
import ufpel.poo.model.Mapa;
public class GerenciadorInimigos {

    private int inimigosPendentes;      
    private long ultimoTempoSpawn;
    private final long INTERVALO_SPAWN = 3000; 
    private final int MAX_INIMIGOS_TELA = 5;

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
        
        // sorteia um número de 0 a 99 (100% de chance total)
        int chance = random.nextInt(100); 

        if (dif == Dificuldade.FACIL) {
            if (chance < 30) return new InimigoAgil(x, y, mapa, motor);
            else if (chance < 20) return new InimigoBlindado(x, y, mapa, motor);
            else return new InimigoDefault(x, y, mapa, motor);
            
        } 
        else if (dif == Dificuldade.MEDIO) {
            if (chance < 30) return new InimigoAgil(x, y, mapa, motor);
            else if (chance < 50) return new InimigoBlindado(x, y, mapa, motor);
            else return new InimigoDefault(x, y, mapa, motor);
            
        } 
        else { 
            if (chance < 30) return new InimigoAgil(x, y, mapa, motor);
            else if (chance < 70) return new InimigoBlindado(x, y, mapa, motor);
            else return new InimigoDefault(x, y, mapa, motor);
        }
    }

    public int getPendentes() {
        return inimigosPendentes;
    }
}