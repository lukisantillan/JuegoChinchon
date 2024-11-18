package ar.edu.unlu.poo2024.chinchon.modelos.carta;

import ar.edu.unlu.poo2024.chinchon.modelos.Palos;

import java.io.Serializable;

public class CartaComodin extends Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    public CartaComodin(Palos palo) {
        super(palo);
    }

    @Override
    public String tipoDeCarta() {
        return "Comodín";
    }

    @Override
    public int getNumero() {
        return -1;
    }
}
