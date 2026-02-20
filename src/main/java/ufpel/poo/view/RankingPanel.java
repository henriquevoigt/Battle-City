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

        JLabel titulo = new JLabel("TOP 10 - RANKING", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 45));
        titulo.setForeground(Color.RED);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JTextArea areaRanking = new JTextArea();
        areaRanking.setEditable(false);

        areaRanking.setFont(new Font("Monospaced", Font.BOLD, 24));
        areaRanking.setBackground(Color.BLACK);
        areaRanking.setForeground(Color.WHITE);

        areaRanking.setMargin(new Insets(20, 160, 20, 20));

        JScrollPane scroll = new JScrollPane(areaRanking);
        scroll.setBorder(null); 
        add(scroll, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setPreferredSize(new Dimension(250, 50));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 18));

        btnVoltar.addActionListener(e -> janela.mostrarMenu());

        JPanel painelSul = new JPanel();
        painelSul.setBackground(Color.BLACK); 
        painelSul.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
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

            area.setText(String.format("%-4s %-15s %s\n", "POS", "NOME", "PONTOS"));
            area.append("----------------------------------\n");

            int limite = Math.min(10, lista.size());
            for (int i = 0; i < limite; i++) {
                String[] jogador = lista.get(i);
                area.append(String.format("%-4d %-15s %06d\n", (i + 1), jogador[1], Integer.parseInt(jogador[0])));
            }

        } catch (Exception e) {
            area.setText("Erro ao carregar ranking.");
            e.printStackTrace();
        }
    }
}