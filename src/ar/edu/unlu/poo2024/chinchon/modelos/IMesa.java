package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IMesa extends IObservableRemoto {

    int agregarJugador(String nombre) throws RemoteException;

    void repartir(int idJugador, int n) throws RemoteException ;

    void reiniciarDescarte() throws RemoteException;

    int calcularPuntajeFinal(int idJugador) throws RemoteException;

    ArrayList<Jugador> getJugadores() throws RemoteException;

    MazoPrincipal getMazoPrincipal() throws RemoteException;

    PozoDescarte getPozoDescarte() throws RemoteException;

    Jugador getJugador(int i) throws RemoteException;

    void comenzarJuego(int idJugador) throws RemoteException;

    ManejadorTurnos getManejadorTurnos() throws RemoteException;

    void reiniciarRonda() throws RemoteException;

    boolean robarParaJugadorMazo(int IdJugador) throws RemoteException;

    boolean robarParaJugadorDescarte(int IdJugador) throws RemoteException;

    boolean descartarCarta(int idJugador, int iCarta) throws RemoteException;

    void cerrar() throws RemoteException;

    void pasarTurno() throws RemoteException;

    boolean puedeCortar(int idJugador) throws RemoteException;

    ArrayList<ArrayList<Carta>> encontrarGrupos(ArrayList<Carta> mano, int cantidad) throws RemoteException;
}
