package ar.edu.unlu.poo2024.chinchon.observer;

import ar.edu.unlu.poo2024.chinchon.modelos.Eventos;

public interface Observable {
    public void notificar(Eventos evento) throws Exception;

    public void agregarObservador(Observador observador);
}
