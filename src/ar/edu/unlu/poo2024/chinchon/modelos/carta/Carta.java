package ar.edu.unlu.poo2024.chinchon.modelos.carta;

import ar.edu.unlu.poo2024.chinchon.modelos.Palos;

import java.io.Serializable;

public abstract class Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Palos palo;
    private Boolean perteneceAgrupo = false;

    public Carta(Palos palo){
        this.palo = palo;
    }

    public boolean perteneceAgrupo() {
        return perteneceAgrupo;
    }

    public boolean esComodin(){
        return true;
    }

    public abstract String tipoDeCarta();
    public abstract int getNumero();

    public String toString() {
        String s;
        if (this.palo != Palos.COMODIN)
            s = this.tipoDeCarta() + " - " + palo.getValor();
        else
            s = this.tipoDeCarta();
        return s;
    }

    public Palos getPalo() {
        return palo;
    }

    public void setPerteneceAgrupo(Boolean perteneceAgrupo) {
        this.perteneceAgrupo = perteneceAgrupo;
    }
}
