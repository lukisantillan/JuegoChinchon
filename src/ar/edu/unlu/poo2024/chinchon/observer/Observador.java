package ar.edu.unlu.poo2024.chinchon.observer;

import ar.edu.unlu.poo2024.chinchon.modelos.Eventos;

public interface Observador {
    public void actualizar(Eventos evento, Observable observado) throws Exception;
}
