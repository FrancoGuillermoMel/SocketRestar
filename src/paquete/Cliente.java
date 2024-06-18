package paquete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        String direccionServidor = "localhost";
        int puerto = 12345;

        try ( Socket servidorSocket = new Socket(direccionServidor, puerto);  BufferedReader lector = new BufferedReader(new InputStreamReader(servidorSocket.getInputStream()));  PrintWriter escritor = new PrintWriter(servidorSocket.getOutputStream(), true);  BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor. Introduce números para restar:");

            String mensajeUsuario;
            while ((mensajeUsuario = entradaUsuario.readLine()) != null) {
                escritor.println(mensajeUsuario);
                String respuestaServidor = lector.readLine();
                System.out.println("Respuesta del servidor: " + respuestaServidor);

                if (respuestaServidor.contains("El servidor llegó al número 0 o inferior.")) {
                    System.out.println("Conexión cerrada por el servidor.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
