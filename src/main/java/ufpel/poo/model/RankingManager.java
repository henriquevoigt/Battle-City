package ufpel.poo.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RankingManager {

    private static final String NOME_ARQUIVO = "ranking.txt";

    public static void salvarPontuacao(String nome, int pontos) {
        List<String[]> lista = new ArrayList<>();

        String nomeSeguro = (nome == null || nome.trim().isEmpty()) ? "Anonimo" : nome.trim();

        try (BufferedReader br = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.trim().isEmpty()) continue;
                
                String[] partes = linha.split(";");

                if (partes.length == 2) {
                    lista.add(partes);
                }
            }
        } catch (IOException e) {
        }

        lista.add(new String[]{String.valueOf(pontos), nomeSeguro});

        lista.sort((a, b) -> {
            try {
                int pontosA = Integer.parseInt(a[0].trim());
                int pontosB = Integer.parseInt(b[0].trim());
                return pontosB - pontosA;
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
            writer.flush();
        } catch (IOException e) {
            System.err.println("Erro crítico ao salvar ranking: " + e.getMessage());
            e.printStackTrace();
        }
    }
}