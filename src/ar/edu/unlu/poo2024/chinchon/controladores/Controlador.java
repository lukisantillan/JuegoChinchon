package ar.edu.unlu.poo2024.chinchon.controladores;

import ar.edu.unlu.poo2024.chinchon.modelos.Eventos;
import ar.edu.unlu.poo2024.chinchon.modelos.IMesa;
import ar.edu.unlu.poo2024.chinchon.modelos.Jugador;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;
import ar.edu.unlu.poo2024.chinchon.vistas.IVista;
import ar.edu.unlu.poo2024.chinchon.vistas.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class Controlador implements IControladorRemoto {
    private IMesa modelo;
    private IVista vista;


    public Controlador(IVista vista) throws Exception, RemoteException {
        this.vista = vista;
    }

    public int sumarPuntos(int idJugador) throws RemoteException {
        try{
            return this.modelo.calcularPuntajeFinal(idJugador);
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public boolean haySuficientesJugadores() throws RemoteException {
        try {
            return this.modelo.getJugadores().size() > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ArrayList<Carta>> encontrarGrupos(ArrayList<Carta> mano, int cantidad) throws RemoteException {
        try {
            return this.modelo.encontrarGrupos(mano, cantidad);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int agregarJugador(String nombre) throws RemoteException {
        try {
            return this.modelo.agregarJugador(nombre);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Jugador jugadorTurnoActual() throws RemoteException {
        try {
            return this.modelo.getJugador(this.modelo.getManejadorTurnos().getTurnoActual());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean puedeRobar() throws RemoteException {
        try {
            return this.modelo.getMazoPrincipal().puedeRobar();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTopeDescarte() throws RemoteException {
        try {
            return this.modelo.getPozoDescarte().verTope().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int tamanioManoJugador(int id) throws RemoteException, Exception {
        try {
            return this.modelo.getJugador(id).getMano().size();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean descartarCarta(int idJugador, int iCarta) throws RemoteException {
        try {
            return this.modelo.descartarCarta(idJugador, iCarta);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void robarParaJugadorMazo(int idJugador) throws RemoteException {
        try {
            this.modelo.robarParaJugadorMazo(idJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pasarTurno() throws RemoteException {
        try {
            this.modelo.pasarTurno();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void robarParaJugadorDescarte(int idJugador) throws RemoteException {
        try {
            this.modelo.robarParaJugadorDescarte(idJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jugador getJugador(int id) throws RemoteException {
        try {
            return this.modelo.getJugador(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void notificarComienzo(int idJugador) throws RemoteException {
        try {
            this.modelo.comenzarJuego(idJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hayCartasEnDescarte() throws RemoteException {
        try {
            return this.modelo.getPozoDescarte().estaVacia();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean puedeCortar(int idJugador) throws RemoteException {
        try {
            return this.modelo.puedeCortar(idJugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void reiniciarRonda() throws RemoteException {
        try {
            this.modelo.reiniciarRonda();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cerrar() throws RemoteException {
        this.modelo.cerrar();
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IMesa) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object objetos) throws RemoteException {
        try {
            Object[] array = (Object[]) objetos;
            Eventos evento = (Eventos) array[0];
            String jugador;
            String carta;
            switch (evento) {
                case JUGADOR_AGREGADO:
                    String nombre = (String) array[1];
                    this.vista.imprimirCartel("->" + nombre + "Se unio al servidor!");
                    break;
                case CAMBIO_TURNO:
                    this.vista.jugar();
                    break;
                case SALIR:
                    this.vista.salir();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



