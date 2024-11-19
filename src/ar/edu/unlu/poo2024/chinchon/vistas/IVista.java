package ar.edu.unlu.poo2024.chinchon.vistas;

import ar.edu.unlu.poo2024.chinchon.controladores.Controlador;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {

    public void iniciar() throws Exception;

    public void setControlador(Controlador controlador);

    public Controlador getControlador();

    public void formularioJugador() throws Exception;

    public void imprimirCartel(String s);
    

    public void jugar() throws Exception;

    public void mostrarGanador(int id) throws RemoteException;


    public void salir();
}