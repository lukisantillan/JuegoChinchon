package ar.edu.unlu.poo2024.chinchon;

import ar.edu.unlu.poo2024.chinchon.vistas.IVista;
import ar.edu.unlu.poo2024.chinchon.vistas.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;

public class AppClienteConsola2 {
    public static void main(String[] args) throws Exception {
        // PARA PRUEBAS
        String ip = "127.0.0.1";
        String ipServidor = "127.0.0.1";
        String portServidor = "8888";
        String port = "9991";

        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        IVista vista = new VistaConsola();
        try {
            c.iniciar(vista.getControlador());
            System.out.println("Cliente corriendo con exito en " + "(" + ip + ":" + port + ")");
            vista.iniciar();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
