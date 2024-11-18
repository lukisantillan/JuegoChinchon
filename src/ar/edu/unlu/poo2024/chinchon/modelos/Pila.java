package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

public class Pila implements Serializable{
    private static final long serialVersionUID = 1L;

    protected Stack<Carta> pilaCartas;
    //FLAG PARA VER SI UN JUGADOR PUEDE ROBAR DEL MAZO o DESCARTE ASI NO PUEDE VOLVER A ROBAR EN EL MISMO TURNO
    private boolean puedeRobar = true;

    public Pila() {
        pilaCartas = new Stack<Carta>();
    }
    public Carta sacar(){
        if (this.estaVacia()){
            throw new EmptyStackException();
        }
        return pilaCartas.pop();
    }

    public boolean estaVacia(){
        return pilaCartas.empty();
    }

    public void agregar(Carta c){
        pilaCartas.push(c);
    }

    // Getters y Setters

    public boolean puedeRobar() {
        return puedeRobar;
    }

    public void setPuedeRobar(boolean puedeRobar) {
        this.puedeRobar = puedeRobar;
    }
}
