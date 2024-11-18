package ar.edu.unlu.poo2024.chinchon.modelos.carta;

import ar.edu.unlu.poo2024.chinchon.modelos.Palos;

import java.io.Serializable;

public class CartaNormal extends Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    int numero; //(1,2,3,4,5,6,7,10,11,12)
    public CartaNormal(Palos palo, int numero){
        super(palo);
        this.numero = numero;
    }

    @Override
    public boolean esComodin(){
        return false;
    }
    @Override
    public String tipoDeCarta() {
        return String.valueOf(getNumero());
    }

    //Getter
    public int getNumero(){
        return numero;
    }
}
