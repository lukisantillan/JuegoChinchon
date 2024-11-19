package ar.edu.unlu.poo2024.chinchon.modelos;

import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.CartaNormal;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
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
    //FALTA VALIDAR CUANDO PUEDE CORTAR
    public boolean puedeCortar(int idJugador) throws RemoteException{
        return true;
    }

    @Override
    public int calcularPuntajeFinal(int idJugador) throws RemoteException {
        int puntos = 0;
        Jugador j = jugadores.get(idJugador);
        ArrayList<Carta> manoJugador = j.getMano();
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

    // NO ESTA EN UML
    // VALIDADOR DE GRUPOS
    private boolean esGrupoValido(ArrayList<Carta> cartas) throws RemoteException {
        Map<Integer, List<Carta>> cartasPorNumero = new HashMap<>();
        List<Carta> comodines = new ArrayList<>();

        // Separar cartas por número y almacenar comodines
        for (Carta carta : cartas) {
            if (carta.esComodin()) {
                comodines.add(carta);
            } else if (carta instanceof CartaNormal) {
                int numero = ((CartaNormal) carta).getNumero();
                cartasPorNumero.putIfAbsent(numero, new ArrayList<>());
                cartasPorNumero.get(numero).add(carta);
            }
        }

        // Buscar un grupo válido
        for (Map.Entry<Integer, List<Carta>> entry : cartasPorNumero.entrySet()) {
            List<Carta> cartasDelNumero = entry.getValue();
            int totalCartas = cartasDelNumero.size() + comodines.size();

            if (totalCartas >= 3) {
                // Validar que las cartas del grupo no pertenezcan ya a otro grupo
                for (Carta carta : cartasDelNumero) {
                    if (carta.perteneceAgrupo()) {
                        return false; // Si alguna carta del grupo ya pertenece a otro grupo, no es válido
                    }
                }

                // Marcar las cartas como pertenecientes al grupo
                for (Carta carta : cartasDelNumero) {
                    carta.setPerteneceAgrupo(true); // Cambiar el atributo
                }

                // Marcar los comodines usados en el grupo
                int comodinesUsados = 3 - cartasDelNumero.size();
                for (int i = 0; i < comodinesUsados && i < comodines.size(); i++) {
                    comodines.get(i).setPerteneceAgrupo(true); // Cambiar el atributo
                }

                return true;
            }
        }

        return false;
    }

    //VALIDADOR ESCALERA
    private boolean esEscaleraValida(ArrayList<Carta> cartas) throws RemoteException {
        Map<Palos, ArrayList<Integer>> cartasPorPalo = new HashMap<>();
        int comodines = 0;

        // Dividir las cartas por palo y contar comodines
        for (Carta carta : cartas) {
            if (carta.esComodin()) {
                comodines++;
            } else if (carta instanceof CartaNormal) {
                Palos palo = carta.getPalo();
                int numero = ((CartaNormal) carta).getNumero();
                cartasPorPalo.putIfAbsent(palo, new ArrayList<>());
                cartasPorPalo.get(palo).add(numero);
            }
        }

        // Validar posibles escaleras por palo
        for (Map.Entry<Palos, ArrayList<Integer>> entry : cartasPorPalo.entrySet()) {
            ArrayList<Integer> numeros = entry.getValue();
            Collections.sort(numeros); // Ordenar las cartas por número

            int secuencia = 1;
            int comodinesRestantes = comodines;

            ArrayList<Carta> cartasEnEscalera = new ArrayList<>();
            cartasEnEscalera.add(obtenerCartaPorNumero(cartas, numeros.get(0))); // Primera carta

            for (int i = 1; i < numeros.size(); i++) {
                int diferencia = numeros.get(i) - numeros.get(i - 1);

                if (diferencia == 1) {
                    // Las cartas son consecutivas
                    secuencia++;
                    cartasEnEscalera.add(obtenerCartaPorNumero(cartas, numeros.get(i)));
                } else if (diferencia > 1 && comodinesRestantes >= diferencia - 1) {
                    // Usar comodines para rellenar
                    secuencia += diferencia;
                    comodinesRestantes -= (diferencia - 1);
                    cartasEnEscalera.add(obtenerCartaPorNumero(cartas, numeros.get(i)));
                } else {
                    // Reiniciar la secuencia
                    secuencia = 1;
                    cartasEnEscalera.clear();
                    cartasEnEscalera.add(obtenerCartaPorNumero(cartas, numeros.get(i)));
                }

                // Si la secuencia es válida, chequear si todas las cartas están libres
                if (secuencia >= 3) {
                    // Validar que ninguna carta ya pertenezca a un grupo
                    boolean todasLibres = cartasEnEscalera.stream().allMatch(c -> !c.perteneceAgrupo());
                    if (todasLibres) {
                        // Marcar las cartas como pertenecientes a un grupo
                        cartasEnEscalera.forEach(c -> c.setPerteneceAgrupo(true));
                        return true;
                    }
                }
            }

            // Usar comodines al final
            if (comodinesRestantes > 0) {
                secuencia += comodinesRestantes;
                if (secuencia >= 3) {
                    boolean todasLibres = cartasEnEscalera.stream().allMatch(c -> !c.perteneceAgrupo());
                    if (todasLibres) {
                        cartasEnEscalera.forEach(c -> c.setPerteneceAgrupo(true));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Carta obtenerCartaPorNumero(ArrayList<Carta> cartas, int numero) {
        return cartas.stream()
                .filter(c -> c instanceof CartaNormal && ((CartaNormal) c).getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    @Override
    //SETEO DE LAS CARTAS COMO PERTENECIENTES A GRUPO
    public boolean formarGrupo(ArrayList<Carta> cartas) throws RemoteException {
        boolean retorno = false;
        if (esGrupoValido(cartas)) {
            for (Carta carta : cartas) {
                carta.setPerteneceAgrupo(true);
            }
            retorno = true;
        } else if (esEscaleraValida(cartas)) {
            for (Carta carta : cartas) {
                carta.setPerteneceAgrupo(true);
            }
            retorno = true;
        }
        return retorno;
    }
}
