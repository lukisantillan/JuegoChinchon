package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.CartaNormal;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Formattable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Mesa extends ObservableRemoto implements IMesa, Serializable {

    private ArrayList<Jugador> jugadores;
    private ManejadorTurnos manejadorTurnos;
    private MazoPrincipal mazoPrincipal;
    private PozoDescarte descarte;
    private final int cartasIniciales = 7;
    private boolean cartaTomada; //para controlar que cada vez que quiera descartar, haya tomado una carta

    public Mesa() throws RemoteException {
        this.jugadores = new ArrayList<Jugador>();
        this.mazoPrincipal = new MazoPrincipal();
        this.descarte = new PozoDescarte();
        this.manejadorTurnos = new ManejadorTurnos(jugadores);
        this.descarte.agregar(this.mazoPrincipal.sacar());
    }

    @Override
    public int agregarJugador(String nombre) throws RemoteException {
        Jugador j = new Jugador(nombre, jugadores.size()); //Crea el jugador y le asigna id, el size de arrayjugadores
        jugadores.add(j); //agrega el jugador
        this.repartir(j.getId(), cartasIniciales);
        Object[] array = {Eventos.JUGADOR_AGREGADO, j.getId()};
        this.notificarObservadores(array);
        return j.getId();
    }

    @Override
    public Jugador getJugador(int i) throws RemoteException {
        return jugadores.get(i); //devuelve el jugador
    }

    @Override
    public void comenzarJuego(int idJugador) throws RemoteException {
        if (this.manejadorTurnos.getTurnoActual() == -1){ //esto quiere decir que el juego no arranco
            this.getManejadorTurnos().setTurnoActual(idJugador);
            Object[] array = {Eventos.CAMBIO_TURNO}; //guardo el evento cambio de turno
            this.notificarObservadores(array); //notifico a observadores cambio de estado
        }
    }

    @Override
    public void repartir(int idJugador, int n) throws RemoteException {
        for (int i = 0; i < n; i++) {
            jugadores.get(idJugador).tomarCarta(mazoPrincipal.sacar()); //saca carta del mazo principal
        }
    }

    @Override
    public boolean robarParaJugadorMazo(int idJugador) throws RemoteException {
        if (this.mazoPrincipal.puedeRobar()) {
            this.getJugador(idJugador).tomarCarta(this.mazoPrincipal.sacar());
            this.mazoPrincipal.setPuedeRobar(false);
            this.descarte.setPuedeRobar(false);
            if (this.mazoPrincipal.estaVacia())
                this.reiniciarDescarte();
            return true;
        }
        return false;
    }

    @Override
    public boolean robarParaJugadorDescarte(int idJugador) throws RemoteException {
        if (this.descarte.puedeRobar() && !this.descarte.estaVacia()) {
            this.getJugador(idJugador).tomarCarta(this.descarte.sacar());
            this.mazoPrincipal.setPuedeRobar(false);
            this.descarte.setPuedeRobar(false);
            if (this.mazoPrincipal.estaVacia())
                this.reiniciarDescarte();
            return true;
        }
        return false;
    }

    @Override
    public void reiniciarDescarte() throws RemoteException {
        Carta primeraCarta = this.descarte.sacar(); //saco primer carta del descarte para no perderla
        while (!this.descarte.estaVacia()){
            this.mazoPrincipal.agregar(this.descarte.sacar());
        }
        this.mazoPrincipal.mezclar();
        this.descarte.agregar(primeraCarta);
    }

    @Override
    public boolean descartarCarta(int idJugador, int iCarta) throws RemoteException {
        Carta cartaJugador = jugadores.get(idJugador).getCarta(iCarta);
        String jugador = this.getJugador(idJugador).getNombre();
        this.descarte.agregar(cartaJugador); //agrego carta al descarte
        this.getJugador(idJugador).jugarCarta(iCarta); //elimino carta del jugador
        Object[] array = {Eventos.CARTA_JUGADA,idJugador};
        this.notificarObservadores(array);
        return true;
    }

    @Override
    public void pasarTurno() throws RemoteException {
        this.manejadorTurnos.siguienteTurno();
        this.mazoPrincipal.setPuedeRobar(true);
        this.descarte.setPuedeRobar(true);
        Object[] array = {Eventos.CAMBIO_TURNO};
        this.notificarObservadores(array);
    }

    @Override
    public boolean puedeCortar(int idJugador) throws RemoteException{
        return true;
    }

    @Override
    public int calcularPuntajeFinal(int idJugador) throws RemoteException {
        int puntos = 0;
        Jugador j = jugadores.get(idJugador);
        ArrayList<Carta> manoJugador = j.getMano();
        ArrayList<ArrayList<Carta>> combi = encontrarGrupos(manoJugador,3);
        if (!combi.isEmpty()) {
            for (ArrayList<Carta> a : combi) {
                manoJugador.removeAll(a);
            }
        }
        if (!manoJugador.isEmpty()){
            for (Carta c: manoJugador){
                if (c.esComodin()){
                    puntos += 10;
                } else {
                    puntos += c.getNumero();
                }
            }
        }
        j.sumarPuntos(puntos);
        return puntos;
    }

    @Override
    public void reiniciarRonda() throws RemoteException {
        this.mazoPrincipal = new MazoPrincipal();
        this.descarte = new PozoDescarte();
        this.manejadorTurnos = new ManejadorTurnos(jugadores);
        this.descarte.agregar(this.mazoPrincipal.sacar());
        for (Jugador j : jugadores){
            j.reiniciarMano();
            this.repartir(j.getId(), cartasIniciales);
        }
    }

    @Override
    public void cerrar() throws RemoteException{
        Object[] array = {Eventos.SALIR};
        this.notificarObservadores(array);
    }

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return this.jugadores;
    }

    @Override
    public MazoPrincipal getMazoPrincipal() throws RemoteException {
        return this.mazoPrincipal;
    }

    @Override
    public PozoDescarte getPozoDescarte() throws RemoteException {
        return this.descarte;
    }

    @Override
    public ManejadorTurnos getManejadorTurnos() throws RemoteException {
        return this.manejadorTurnos;
    }

    @Override
    public ArrayList<ArrayList<Carta>> encontrarGrupos(ArrayList<Carta> mano, int cantidad) {
        HashMap<Integer, ArrayList<Carta>> cartasPorNumero = new HashMap<>();

        // Agrupamos las cartas por su número
        for (Carta carta : mano) {
            int numero = carta.getNumero();
            cartasPorNumero.putIfAbsent(numero, new ArrayList<>());
            cartasPorNumero.get(numero).add(carta);
        }

        // Filtramos los grupos que cumplen con la cantidad mínima requerida
        ArrayList<ArrayList<Carta>> grupos = new ArrayList<>();
        for (ArrayList<Carta> grupo : cartasPorNumero.values()) {
            if (grupo.size() >= cantidad) {
                grupos.add(grupo);
            }
        }

        return grupos;
    }
}
