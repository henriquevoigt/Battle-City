package ufpel.poo.model;

import java.awt.Rectangle;

public interface IValidadorMovimento {

    boolean isPosicaoLivre(Rectangle retanguloFuturo, EntidadeDinamica quemSolicitou);
    
}