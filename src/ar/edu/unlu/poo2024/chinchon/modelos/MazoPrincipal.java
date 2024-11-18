package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.CartaComodin;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.CartaNormal;

import java.io.Serializable;
import java.util.Collections;

public class MazoPrincipal extends Pila implements Serializable{
    private static final long serialVersionUID = 1L;

    public MazoPrincipal(){
        super();
        this.crear();
        this.mezclar();
    }

    private void crear(){
        for (Palos palo : Palos.values()){
            if (palo != Palos.COMODIN) {
                for (int numero = 1; numero <= 12; numero++) {
                    if (numero <= 7 || numero >= 10) {
                        pilaCartas.add(new CartaNormal(palo, numero));
                    }
                }
            }
        }
        //Creo dos comodines
        pilaCartas.add(new CartaComodin(Palos.COMODIN));
        pilaCartas.add(new CartaComodin(Palos.COMODIN));
    }

    public void mezclar(){
        //mezcla
        Collections.shuffle(this.pilaCartas);
    }

}
