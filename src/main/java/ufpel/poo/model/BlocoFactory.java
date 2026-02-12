package ufpel.poo.model;

public class BlocoFactory {

    private static final int TAM = 40;

    public static Bloco criar(int tipo, int gridX, int gridY) {
        int x = gridX * TAM;
        int y = gridY * TAM;

        switch (tipo) {
            case 1: return new Tijolo(x, y);
            case 2: return new Arvore(x, y);
            case 3: return new Agua(x, y);
            case 4: return new Aco(x, y);
            default: return new Vazio(x, y);
        }
    } 
}
