package ufpel.poo.interfaces;

import ufpel.poo.model.EntidadeDinamica;

import java.awt.Rectangle;

public interface IValidadorMovimento {

    boolean isPosicaoLivre(Rectangle retanguloFuturo, EntidadeDinamica quemSolicitou);
    
}