package ar.edu.unlu.poo2024.chinchon;

import ar.edu.unlu.poo2024.chinchon.vistas.IVista;
import ar.edu.unlu.poo2024.chinchon.vistas.VistaConsola;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;

public class AppClienteConsola {
    public static void main(String[] args) throws Exception {
        // PARA PRUEBAS
        String ip = "127.0.0.1";
        String ipServidor = "127.0.0.1";
        String portServidor = "8888";
        String port = "9999";

        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        IVista vista = new VistaConsola();
        vista.iniciar();
        try {
            c.iniciar(vista.getControlador());
            System.out.println("Cliente corriendo con exito en " + "(" + ip + ":" + port + ")");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
