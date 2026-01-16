package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private Janela janelaPrincipal;

    public MenuPanel(Janela janela) {
        this.janelaPrincipal = janela;
        
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);     

        JLabel titulo = new JLabel("Battlengana 1");
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(Color.ORANGE);
        
        JButton btnJogar = criarBotao("1 - Jogar");
        JButton btnRanking = criarBotao("2 - Ranking");
        JButton btnSair = criarBotao("3 - Sair");

        btnJogar.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Digite seu nome, soldado:");
            if (nome != null && !nome.isEmpty()) {
                janelaPrincipal.mostrarSelecaoMapa(nome);
            }
        });

        btnSair.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.insets = new Insets(10, 0, 30, 0);
        add(titulo, gbc);

        gbc.gridy++; gbc.insets = new Insets(10, 0, 10, 0);
        add(btnJogar, gbc);
        
        gbc.gridy++;
        add(btnRanking, gbc);

        gbc.gridy++;
        add(btnSair, gbc);
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        return btn;
    }
}