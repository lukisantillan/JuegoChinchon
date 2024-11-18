package ar.edu.unlu.poo2024.chinchon.modelos;

import java.io.Serializable;

public enum Palos implements Serializable{
    BASTO("BASTO"),ESPADA("ESPADA"), ORO("ORO"), COPA("COPA"), COMODIN("COMODÍN");

    private final String valor;

    Palos(String v){
        this.valor = v;
    }

    public String getValor(){
        return valor;
    }
}
