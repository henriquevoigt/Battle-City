package ufpel.poo.view;

import javax.swing.JFrame;
import ufpel.poo.model.ConfiguracaoJogo;

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
        MenuConfiguracao menuConf = new MenuConfiguracao(this, nomeJogador);
        setContentPane(menuConf);
        revalidate();
        repaint();
    }

    // chamada APÓS clicar em "Iniciar Missão"
    public void iniciarJogo(String nomeJogador, ConfiguracaoJogo config) {
        System.out.println("Iniciando jogo para: " + nomeJogador);
        
        TelaJogo jogo = new TelaJogo(nomeJogador, config); 
         
        setContentPane(jogo);
        jogo.requestFocusInWindow();
        revalidate();
        repaint();
    }
    public void mostrarRanking() {
        RankingPanel ranking = new RankingPanel(this);
        setContentPane(ranking);
        revalidate();
        repaint();
    }

}