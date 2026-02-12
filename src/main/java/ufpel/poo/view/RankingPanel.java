package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class RankingPanel extends JPanel {

    public RankingPanel(Janela janela) {
        
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

        JScrollPane scroll = new JScrollPane(areaRanking);
        scroll.setBorder(null); 
        add(scroll, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setPreferredSize(new Dimension(200, 50));

        btnVoltar.addActionListener(e -> janela.mostrarMenu());

        JPanel painelSul = new JPanel();
        painelSul.setBackground(Color.BLACK); 
        painelSul.add(btnVoltar);
        add(painelSul, BorderLayout.SOUTH);
        
        carregarRanking(areaRanking);
    }

    private void carregarRanking(JTextArea area) {
        File arquivo = new File("ranking.txt");

        if (!arquivo.exists()) {
            area.setText("\n\n      Nenhum recorde salvo ainda.\n      Jogue para entrar no Rank!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            
            List<String[]> lista = new ArrayList<>();
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    lista.add(partes);
                }
            }

            if (lista.isEmpty()) {
                area.setText("\n\n      Nenhum recorde salvo ainda.");
                return;
            }

            lista.sort((a, b) -> {
                try {
                    int pontosA = Integer.parseInt(a[0].trim());
                    int pontosB = Integer.parseInt(b[0].trim());
                    return pontosB - pontosA; 
                } catch (NumberFormatException e) {
                    return 0;
                }
            });

            area.setText(String.format("%-15s %s\n", "NOME", "PONTOS"));
            area.append("----------------------------\n");

            for (String[] jogador : lista) {
                area.append(String.format("%-15s %s\n", jogador[1], jogador[0]));
            }

        } catch (Exception e) {
            area.setText("Erro ao carregar ranking.");
            e.printStackTrace();
        }
    }
}