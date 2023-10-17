package dev.joseluisgs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicLong;

public class Server {
    private static final AtomicLong clientNumber = new AtomicLong(0);
    private final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        try {
            // Nos anunciamos como socket
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Servidor escuchando en el puerto 3000");

            // Nos ponemos a escuchar, cada petición la atendemos en un hilo para no bloquear el main
            while (true) {
                new ClientHandler(serverSocket.accept(), clientNumber.incrementAndGet()).start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}