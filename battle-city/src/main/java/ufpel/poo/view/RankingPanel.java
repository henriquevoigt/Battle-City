package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class RankingPanel extends JPanel {

    private Janela janela;

    public RankingPanel(Janela janela) {
        this.janela = janela;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel titulo = new JLabel("RANKING", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.ORANGE);
        add(titulo, BorderLayout.NORTH);

        JTextArea areaRanking = new JTextArea();
        areaRanking.setEditable(false);
        areaRanking.setFont(new Font("Monospaced", Font.PLAIN, 18));
        areaRanking.setBackground(Color.BLACK);
        areaRanking.setForeground(Color.WHITE);

        carregarRanking(areaRanking);

        JScrollPane scroll = new JScrollPane(areaRanking);
        add(scroll, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> janela.mostrarMenu());

        JPanel painelSul = new JPanel();
        painelSul.add(btnVoltar);
        add(painelSul, BorderLayout.SOUTH);
    }

    private void carregarRanking(JTextArea area) {
        try {
            InputStream is = getClass().getResourceAsStream("/ranking.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            List<String[]> lista = new ArrayList<>();
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                lista.add(partes);
            }

            // ordenar por pontuação (decrescente)
            lista.sort((a, b) -> Integer.parseInt(b[0]) - Integer.parseInt(a[0]));

            for (String[] jogador : lista) {
                area.append(String.format("%-10s %s\n", jogador[0], jogador[1]));
            }

        } catch (Exception e) {
            area.setText("Erro ao carregar ranking.");
        }
    }
}
