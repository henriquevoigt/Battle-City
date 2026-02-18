package ufpel.poo.model;
public class ConfiguracaoJogo {

    private Dificuldade dificuldade;
    private TipoTanque tipoTanque;
    private int indiceMapa;

    public ConfiguracaoJogo(Dificuldade dificuldade, TipoTanque tipoTanque, int indiceMapa) {
        this.dificuldade = dificuldade;
        this.tipoTanque = tipoTanque;
        this.indiceMapa = indiceMapa;
    }

    public Dificuldade getDificuldade() { 
        return dificuldade; 
    }
    public TipoTanque getTipoTanque() { 
        return tipoTanque; 
    }
    public int getIndiceMapa() { 
        return indiceMapa; 
    }
}