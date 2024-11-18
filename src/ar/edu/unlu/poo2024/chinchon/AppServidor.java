package ar.edu.unlu.poo2024.chinchon;

import ar.edu.unlu.poo2024.chinchon.modelos.Mesa;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import java.rmi.RemoteException;

public class AppServidor {
    public static void main(String[] args) throws RemoteException {
        String ip = "127.0.0.1";
        String port = "8888";

        Mesa modelo = new Mesa();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            servidor.iniciar(modelo);
            System.out.println("Servidor corriendo con exito en " + "(" + ip + ":" + port + ")");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
