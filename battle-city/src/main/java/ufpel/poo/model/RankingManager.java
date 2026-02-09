package ufpel.poo.model;

import java.io.*;
import java.net.URL;

public class RankingManager {

    private static File getArquivoRanking() throws Exception {
        URL url = RankingManager.class.getResource("/ranking.txt");
        if (url == null) {
            throw new FileNotFoundException("ranking.txt não encontrado em resources!");
        }
        return new File(url.toURI());
    }

    public static void salvarPontuacao(String nome, int pontos) {
        try {
            File arquivo = getArquivoRanking();
            FileWriter fw = new FileWriter(arquivo, true);
            fw.write(pontos + ";" + nome + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
