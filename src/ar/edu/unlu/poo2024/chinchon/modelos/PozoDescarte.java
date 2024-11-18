package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;

import java.io.Serializable;
import java.util.EmptyStackException;

public class PozoDescarte extends Pila implements Serializable{
    private static final long serialVersionUID = 1L;

    public PozoDescarte(){
        super();
    }

    public Carta verTope() {
        if (this.estaVacia()) {
            throw new EmptyStackException();
        }
        return this.pilaCartas.peek();
    }
}
