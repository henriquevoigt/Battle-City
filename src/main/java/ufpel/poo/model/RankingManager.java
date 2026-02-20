package ufpel.poo.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RankingManager {

    private static final String NOME_ARQUIVO = "ranking.txt";

    public static void salvarPontuacao(String nome, int pontos) {
        List<String[]> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                lista.add(linha.split(";"));
            }
        } catch (IOException e) {
        }

        lista.add(new String[]{String.valueOf(pontos), nome});

        lista.sort((a, b) -> {
            try {
                return Integer.parseInt(b[0].trim()) - Integer.parseInt(a[0].trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        });

        if (lista.size() > 10) {
            lista = lista.subList(0, 10);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, false))) {
            for (String[] registro : lista) {
                writer.write(registro[0] + ";" + registro[1]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }
}