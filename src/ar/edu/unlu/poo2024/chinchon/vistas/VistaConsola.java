package ar.edu.unlu.poo2024.chinchon.vistas;

import ar.edu.unlu.poo2024.chinchon.controladores.Controlador;
import ar.edu.unlu.poo2024.chinchon.modelos.Jugador;
import ar.edu.unlu.poo2024.chinchon.modelos.carta.Carta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.Objects;

public class VistaConsola extends JFrame implements IVista {
    private static final long serialVersionUID = 1L;

    private JTextArea textArea;
    private JPanel panelPrincipal;
    private JTextField inputField;
    private JButton btnEnter;


    private int clienteID;
    private Controlador controlador;
    private Estados estado;
    private Jugador jugadorConMenosPuntaje;

    public VistaConsola() throws RemoteException,Exception {
        Controlador controlador = new Controlador(this);
        this.jugadorConMenosPuntaje = null;
        this.setControlador(controlador);
        this.inicializarConsola();
    }

    private void inicializarConsola() {
        setTitle("-> CHINCHON <-");
        getContentPane().setLayout(new BorderLayout());
        setSize(600, 500);
        // Llamar a metodo al cerrar
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    controlador.cerrar();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                try {
                    if (!inputText.trim().equals(""))
                        procesarEntrada(inputText.trim());
                } catch (Exception e1) {
                    System.err.print("Error en el input. Solo puede ingresar valores numericos\n");
                }
                inputField.setText("");
            }
        });

        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        getContentPane().add(inputField, BorderLayout.SOUTH);
    }

    private void print(String string) {
        textArea.append(string);
    }

    private void println(String string) {
        print(string + "\n");
    }


    @Override
    public void iniciar() throws Exception {
        setVisible(true);
        this.estado = Estados.AGREGAR_JUGADOR;
        this.formularioJugador();
    }

    private void procesarEntrada(String textoDeEntrada) throws Exception{
        switch (estado){
            case MENU_PRINCIPAL:
                setTitle("-> MENU PRINCIPAL <-");
                this.menu(textoDeEntrada);
                break;
            case AGREGAR_JUGADOR:
                this.clienteID = this.controlador.agregarJugador(textoDeEntrada);
                this.volverAlMenuPrincipal();
                break;
            case JUEGO:
                if (clienteID == 1) { // Solo el primer jugador puede iniciar
                    this.controlador.notificarComienzo(clienteID);
                    String nombreJugador = this.getControlador().getJugador(this.clienteID).getNombre();
                    setTitle("CHINCHON -> JUGANDO ");
                    this.jugar();
                } else {
                    textArea.append("... <-\n");
                }
                break;
            case ESPERANDO_JUGADA:
            case ROBAR_CARTA:
            case TIRAR_CARTA:
            case CORTAR:
                setTitle("CHINCHON -> PROCESANDO JUGADA... <-");
                this.procesarJugada(textoDeEntrada);
                break;
            case CAMBIANDO_RONDA:
                this.cambiarRonda();
                break;
            case SALIR:
                if (textoDeEntrada.equals("0")) this.salir();
                break;
            default:
                break;
        }
    }


    private void menu(String opcion) throws Exception {
        switch (opcion) {
            case "1":
                if (this.controlador.haySuficientesJugadores()) {
                    this.estado = Estados.JUEGO;
                    this.procesarEntrada(opcion);
                } else {
                    this.volverAlMenuPrincipal();
                    this.imprimirCartel("No hay suficientes jugadores para comenzar");
                }
                break;
            case "0":
                System.exit(0);
                break;
        }
    }

    private void procesarJugada(String textoEntrada) throws Exception {
        this.imprimirCartel("ENTRADA " +
                "=" + textoEntrada);
        int IDjugador = this.controlador.jugadorTurnoActual().getId();
        if (IDjugador == this.clienteID) {
            switch (estado){
                case ESPERANDO_JUGADA:
                    this.imprimirCartel("ENTRADA " +
                            "=" + textoEntrada);
                    if (textoEntrada.equals("1") && this.controlador.puedeRobar()){
                        this.imprimirCartel("-> SE ESTA TOMANDO UNA CARTA DEL MAZO...");
                        this.controlador.robarParaJugadorMazo(IDjugador);
                    } else if (textoEntrada.equals("2") && this.controlador.puedeRobar() && !this.controlador.hayCartasEnDescarte()){
                        this.imprimirCartel("-> SE ESTA TOMANDO UNA CARTA DEL DESCARTE...");
                        this.controlador.robarParaJugadorDescarte(IDjugador);;
                    } else {
                        this.imprimirCartel("-> INGRESA UN VALOR VALIDO");
                        break;
                    }
                    this.estado = Estados.ROBAR_CARTA;
                    this.mostrarManoJugador();
                    this.imprimirCartel("SELECCIONE LA CARTA A DESCARTAR");
                    break;
                case ROBAR_CARTA:
                    int numero = Integer.parseInt(textoEntrada);
                    this.imprimirCartel("DESCARTANDO LA CARTA " + numero + " QUE ES-> " + this.controlador.getJugador(IDjugador).getCarta(numero -1));
                    this.controlador.descartarCarta(IDjugador,numero - 1);
                    if (this.controlador.puedeCortar(IDjugador)){
                        this.imprimirCartel("DESEA CORTAR LA MANO: ");
                        this.imprimirCartel("[1] - SI");
                        this.imprimirCartel("[2] - NO");
                        this.estado = Estados.CORTAR;
                    } else if (!this.controlador.puedeCortar(IDjugador)){
                        this.imprimirCartel("DESEA PASAR LA RONDA - >");
                        this.estado = Estados.TIRAR_CARTA;
                    }
                    break;
                case TIRAR_CARTA:
                    this.imprimirCartel("PASANDO RONDA.. ");
                    this.controlador.pasarTurno();
                    this.estado = Estados.JUEGO;
                    this.procesarEntrada(textoEntrada);
                    break;
                case CORTAR:
                    numero = Integer.parseInt(textoEntrada);
                    if (numero == 2){
                        this.estado = Estados.TIRAR_CARTA;
                        break;
                    }
                    this.controlador.sumarPuntos(IDjugador);
                    this.imprimirCartel("EL JUGADOR TIENE LOS SIGUIENTES PUNTOS : " + this.controlador.getJugador(IDjugador).getPuntaje());
                    if (this.controlador.getJugador(IDjugador).getPuntaje() >= 50){
                        //Jugador eliminado.
                    } else if (this.jugadorConMenosPuntaje == null){
                        this.jugadorConMenosPuntaje = this.controlador.getJugador(IDjugador);
                    } else if(this.jugadorConMenosPuntaje.getPuntaje() >= this.controlador.getJugador(IDjugador).getPuntaje()){
                        this.jugadorConMenosPuntaje = this.controlador.getJugador(IDjugador);
                    }
                    this.imprimirCartel("EL JUGADOR CON MENOS PUNTAJE ES : " + this.jugadorConMenosPuntaje.getNombre());
                    this.imprimirCartel("DESEA PASAR LA RONDA - >");
                    this.estado = Estados.TIRAR_CARTA;
                    break;
            }
        }
    }



    private void cambiarRonda() throws RemoteException {
        this.controlador.reiniciarRonda();
        this.estado = Estados.JUEGO;
    }

    @Override
    public void jugar() throws RemoteException {
        this.imprimirCartel("-> EJECUTANDO JUGAR");
        int IDjugador = this.controlador.jugadorTurnoActual().getId();
        if (IDjugador == clienteID){
            inputField.setEnabled(true);
            this.mostrarManoJugador();
            this.imprimirCartel("-----");
            this.imprimirCartel("DESCARTE -->" + this.controlador.getTopeDescarte() + "<--");
            this.imprimirCartel("-----");
            this.imprimirCartel("->[1] PARA ROBAR DEL MAZO");
            this.imprimirCartel("->[2] PARA ROBAR DEL DESCARTE");
            this.estado = Estados.ESPERANDO_JUGADA;
        } else {
            String nombre = this.controlador.getJugador(IDjugador).getNombre();
            textArea.setText("Turno de " + nombre + ". Esperando...");
            inputField.setEnabled(false);
        }
    }


    private void volverAlMenuPrincipal() {
        this.estado = Estados.MENU_PRINCIPAL;
        this.mostrarMenuPrincipal();
    }

    public void mostrarMenuPrincipal() {
        textArea.setText("-------------------->  CHINCHON  <--------------------\n" + "\n"
                + "Selecciona una opcion:\n" + "1. Para comenzar partida\n"  + "0. Salir\n");
    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public Controlador getControlador() {
        return this.controlador;
    }

    @Override
    public void formularioJugador() throws Exception {
        textArea.setText("Ingrese su nombre: ");
    }

    @Override
    public void imprimirCartel(String s) {
        textArea.append("\n" + s);
    }


    @Override
    public void mostrarGanador(int id) throws RemoteException {
        this.imprimirCartel("EL GANADOR DE LA PARTIDA ES: ");
        this.imprimirCartel("---> " + this.controlador.getJugador(id).getNombre() + " con " + this.controlador.getJugador(id).getPuntaje());
    }

    @Override
    public void salir() {
        textArea.setText("Un jugador ha salido. Presiona 0 para salir...");
        this.estado = Estados.SALIR;
    }

    @Override
    public void mostrarManoJugador() throws RemoteException {
        String s = "-------------------------Mano de " + this.controlador.jugadorTurnoActual().getNombre() + "----------------------------\n";
        int i = 1;
        for (Carta c: this.controlador.jugadorTurnoActual().getMano()){
            this.imprimirCartel( "Indice: (" + i + ") " + c.toString());
            i++;
        }
        s = s + "------------------------------------------------------------------------------------";
    }
}
