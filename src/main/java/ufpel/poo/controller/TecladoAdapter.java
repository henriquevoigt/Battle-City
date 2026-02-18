package ufpel.poo.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import ufpel.poo.model.EstadoJogo;
import ufpel.poo.view.TelaJogo;

public class TecladoAdapter extends KeyAdapter {

    private GerenciadorJogo controller;
    private TelaJogo view;
    
    // flag das teclas
    private boolean cima, baixo, esquerda, direita, atirar;

    public TecladoAdapter(GerenciadorJogo controller, TelaJogo view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();

        if (codigo == KeyEvent.VK_ESCAPE) {
            controller.alternarPausa();
            view.atualizarVisibilidadeBotoes();
        }

        if (controller.getEstadoAtual() == EstadoJogo.JOGANDO) {
            if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = true;
            if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = true;
            if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = true;
            if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = true;
            if (codigo == KeyEvent.VK_SPACE) atirar = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int codigo = e.getKeyCode();
        
        if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) cima = false;
        if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) baixo = false;
        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) esquerda = false;
        if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) direita = false;
        if (codigo == KeyEvent.VK_SPACE) atirar = false;
    }

    // getters pro gameloop (TelaJogo) consultar
    public boolean isCima() { 
        return cima; 
    }
    public boolean isBaixo() { 
        return baixo; 
    }
    public boolean isEsquerda() { 
        return esquerda; 
    }
    public boolean isDireita() { 
        return direita; 
    }
    public boolean isAtirar() { 
        return atirar; 
    }
    
    public void resetarInputs() {
        cima = false; baixo = false; esquerda = false; direita = false; atirar = false;
    }
}