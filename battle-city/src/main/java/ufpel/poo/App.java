package ufpel.poo;

import javax.swing.SwingUtilities;
import ufpel.poo.view.Janela;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Janela();
        });
    }
}