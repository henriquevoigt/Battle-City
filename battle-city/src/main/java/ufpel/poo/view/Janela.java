package ufpel.poo.view;

import javax.swing.JFrame;

public class Janela extends JFrame {

    public Janela() {
        setTitle("BattleCity");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 720);
        setLocationRelativeTo(null);

        mostrarMenu();

        setVisible(true);
    }

    public void mostrarMenu() {
        MenuPanel menu = new MenuPanel(this);
        setContentPane(menu);
        revalidate();
        repaint();
    }

    // chamada APÓS clicar em "Jogar"
    public void mostrarSelecaoMapa(String nomeJogador) {
        MenuSelecaoMapa menuMapa = new MenuSelecaoMapa(this, nomeJogador);
        setContentPane(menuMapa);
        revalidate();
        repaint();
    }

    // chamada APÓS escolher o mapa
    public void iniciarJogo(String nomeJogador, int indiceMapa) {
        System.out.println("Iniciando jogo para: " + nomeJogador);
        System.out.println("Mapa escolhido: " + indiceMapa);

        TelaJogo jogo = new TelaJogo();
        jogo.carregarMapa(indiceMapa);

        setContentPane(jogo);
        jogo.requestFocusInWindow();

        revalidate();
        repaint();
    }
}
