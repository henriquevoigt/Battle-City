package ufpel.poo.model;

import java.io.*;

public class RankingManager {

    private static final String NOME_ARQUIVO = "ranking.txt";

    public static void salvarPontuacao(String nome, int pontos) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true))) {

            writer.write(pontos + ";" + nome);
            writer.newLine();
            
        } catch (IOException e) {
            System.err.println("Erro ao salvar ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }
}