package ufpel.poo.view;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import ufpel.poo.model.ConfiguracaoJogo;
import ufpel.poo.model.Dificuldade;
import ufpel.poo.model.TipoTanque;

public class MenuConfiguracao extends JPanel {

    private ButtonGroup grupoMapa, grupoDificuldade, grupoTanque;

    public MenuConfiguracao(Janela janela, String nomeJogador) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        add(criarTitulo("CONFIGURAÇÃO DA PARTIDA"));
        add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SELEÇÃO DE MAPA ---
        add(criarSubTitulo("Escolha o Mapa:"));
        grupoMapa = new ButtonGroup();
        JPanel painelMapa = new JPanel();
        painelMapa.setOpaque(false);
        painelMapa.add(criarRadio("Mapa 1", "0", true, grupoMapa));
        painelMapa.add(criarRadio("Mapa 2", "1", false, grupoMapa));
        painelMapa.add(criarRadio("Mapa 3", "2", false, grupoMapa));
        painelMapa.add(criarRadio("Aleatório", "-1", false, grupoMapa));
        add(painelMapa);

        // --- DIFICULDADE ---
        add(criarSubTitulo("Dificuldade:"));
        grupoDificuldade = new ButtonGroup();
        JPanel painelDif = new JPanel();
        painelDif.setOpaque(false);
        painelDif.add(criarRadio("Fácil", "FACIL", false, grupoDificuldade));
        painelDif.add(criarRadio("Médio", "MEDIO", true, grupoDificuldade)); // Default
        painelDif.add(criarRadio("Difícil", "DIFICIL", false, grupoDificuldade));
        add(painelDif);

        // --- TIPO DE TANQUE ---
        add(criarSubTitulo("Seu Tanque:"));
        grupoTanque = new ButtonGroup();
        JPanel painelTanque = new JPanel();
        painelTanque.setOpaque(false);
        painelTanque.add(criarRadio("Ágil (Veloz, -Vida)", "AGIL", false, grupoTanque));
        painelTanque.add(criarRadio("Balanceado", "BALANCEADO", true, grupoTanque));
        painelTanque.add(criarRadio("Blindado (Lento, +Vida)", "BLINDADO", false, grupoTanque));
        add(painelTanque);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- BOTÃO INICIAR ---
        JButton btnIniciar = new JButton("INICIAR MISSÃO");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 20));
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciar.addActionListener(e -> iniciar(janela, nomeJogador));
        add(btnIniciar);
    }

    private void iniciar(Janela janela, String nomeJogador) {
        // 1. Resolver Mapa
        String mapaCmd = grupoMapa.getSelection().getActionCommand();
        int indiceMapa = Integer.parseInt(mapaCmd);
        if (indiceMapa == -1) {
            indiceMapa = new Random().nextInt(3); // 0, 1 ou 2
        }

        // 2. Resolver Dificuldade
        String difCmd = grupoDificuldade.getSelection().getActionCommand();
        Dificuldade dif = Dificuldade.valueOf(difCmd);

        // 3. Resolver Tanque
        String tankCmd = grupoTanque.getSelection().getActionCommand();
        TipoTanque tipo = TipoTanque.valueOf(tankCmd);

        // Cria o objeto de configuração e manda pra janela
        ConfiguracaoJogo config = new ConfiguracaoJogo(dif, tipo, indiceMapa);
        janela.iniciarJogo(nomeJogador, config);
    }

    // --- MÉTODOS AUXILIARES DE UI ---
    
    private JLabel criarTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.ORANGE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel criarSubTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JRadioButton criarRadio(String texto, String comando, boolean selecionado, ButtonGroup grupo) {
        JRadioButton rb = new JRadioButton(texto);
        rb.setActionCommand(comando);
        rb.setSelected(selecionado);
        rb.setOpaque(false);
        rb.setForeground(Color.WHITE);
        grupo.add(rb);
        return rb;
    }
}