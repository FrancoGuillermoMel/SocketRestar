package paquete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*  
Hay 2 clientes, en el servidor va a haber un 
numero (100), cada numero que ingresa se resta 
hasta que llegue a cero y se cierre la sesion
*/


public class Servidor {

    private static int numeroServidor = 100;

    public static void main(String[] args) {
        int puerto = 12345;

        try ( ServerSocket servidorSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                new Thread(new ClienteHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClienteHandler implements Runnable {

        private Socket clienteSocket;

        public ClienteHandler(Socket socket) {
            this.clienteSocket = socket;
        }

        @Override
        public void run() {
            try ( BufferedReader lector = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));  PrintWriter escritor = new PrintWriter(clienteSocket.getOutputStream(), true)) {

                String mensajeCliente;
                while ((mensajeCliente = lector.readLine()) != null) {
                    int valorRestar = Integer.parseInt(mensajeCliente);
                    synchronized (Servidor.class) {
                        numeroServidor -= valorRestar;
                    }

                    if (numeroServidor <= 0) {
                        escritor.println("El servidor llegó al número 0 o inferior.");
                        break;
                    } else {
                        escritor.println("Número restante en el servidor: " + numeroServidor);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
