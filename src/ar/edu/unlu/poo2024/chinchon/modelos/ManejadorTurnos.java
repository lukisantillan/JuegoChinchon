package ar.edu.unlu.poo2024.chinchon.modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class ManejadorTurnos implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Jugador> jugadores;
    private int turnoActual;

    public ManejadorTurnos(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        this.turnoActual = -1; //Todavia no empezo la partida
    }

    //Encapsulamiento,Flexibilidad
    public void siguienteTurno(){
        cambiarTurno();
    }

    private void cambiarTurno(){
        turnoActual = (turnoActual + 1) % jugadores.size(); //Asegura que el turno sea circular.
    }

    //Getter
    public int getTurnoActual(){
        return turnoActual;
    }
    //Setter
    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }
}
