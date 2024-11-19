package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.CartaComodin;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int id;
    private ArrayList<Carta> mano;
    int puntaje;
    private String fechaCreacion;
    private boolean puedeCerrarMano;

    public Jugador(String nombre, int id){
        // primer letra mayuscula, las demas minusculas
        nombre = nombre.substring(0,1).toUpperCase() + nombre.substring(1).toLowerCase();
        this.setNombre(nombre);
        this.setId(id);
        this.mano = new ArrayList<Carta>();
        this.puntaje = 0;
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.setFechaCreacion(fechaHoraActual.format(formato));
        this.puedeCerrarMano = false;
    }

    //Agarra Carta;
    public void tomarCarta(Carta c){
        mano.add(c);
    }

    //LanzaCarta
    public Carta jugarCarta(int i){
        if (i < 0 || i >= mano.size()){
            throw new IndexOutOfBoundsException("Indice de carta invalido");
        }
        return mano.remove(i);
    }

    //MuestraCarta
    public Carta getCarta(int i){
        if (i < 0 || i >= mano.size()){
            throw new IndexOutOfBoundsException("Indice de carta invalido");
        }
        return mano.get(i);
    }
    //sumar puntos
    public void sumarPuntos(int puntos){
        this.puntaje += puntos;
    }

    //reiniciar mano
    public void reiniciarMano(){
        this.mano = new ArrayList<Carta>();
    }

    //funciones para calcular puntaje

    //Getters
    public String getNombre(){
        return nombre;
    }
    public int getPuntaje(){
        return puntaje;
    }

    public int getId(){
        return id;
    }

    public String getFechaCreacion(){
        return fechaCreacion;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setPuedeCerrarMano(Boolean condicion){
        this.puedeCerrarMano = condicion;
    }

}
