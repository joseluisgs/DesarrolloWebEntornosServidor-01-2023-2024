package dev.joseluisgs.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {
        try {
            // Nos anunciamos como socket
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Servidor escuchando en el puerto 3000");

            // Nos ponemos a escuchar, cada petición la atendemos en un hilo para no bloquear el main
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}