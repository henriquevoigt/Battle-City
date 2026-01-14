package ufpel.poo.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
    }

    public void iniciarJogo(String nomeJog) {
        System.out.println("Iniciando jogo para: " + nomeJog);
                
        TelaJogo jogo = new TelaJogo();
        
        setContentPane(jogo);
        
        jogo.requestFocusInWindow();
        
        revalidate();
    }
}