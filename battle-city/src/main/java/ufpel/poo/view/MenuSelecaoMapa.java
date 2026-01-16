package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;

public class MenuSelecaoMapa extends JPanel {

    private Janela janela;
    private String nomeJogador;

    public MenuSelecaoMapa(Janela janela, String nomeJogador) {
        this.janela = janela;
        this.nomeJogador = nomeJogador;

        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titulo = new JLabel("SELECIONE O MAPA");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        gbc.gridy = 0;
        add(titulo, gbc);

        JButton mapa1 = criarBotao("Mapa 1");
        JButton mapa2 = criarBotao("Mapa 2");
        JButton mapa3 = criarBotao("Mapa 3");

        gbc.gridy++;
        add(mapa1, gbc);
        gbc.gridy++;
        add(mapa2, gbc);
        gbc.gridy++;
        add(mapa3, gbc);

        mapa1.addActionListener(e -> janela.iniciarJogo(nomeJogador, 0));
        mapa2.addActionListener(e -> janela.iniciarJogo(nomeJogador, 1));
        mapa3.addActionListener(e -> janela.iniciarJogo(nomeJogador, 2));
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        return btn;
    }
}
