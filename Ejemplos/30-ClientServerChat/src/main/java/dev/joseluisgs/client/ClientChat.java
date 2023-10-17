package dev.joseluisgs.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
    private final String HOST = "localhost";
    private final Logger logger = LoggerFactory.getLogger(ClientChat.class);
    private final int PORT = 3000;


    public static void main(String[] args) throws Exception {
        ClientChat clientChat = new ClientChat();
        clientChat.start(clientChat.HOST, clientChat.PORT);
    }

    public void start(String host, int port) throws Exception {
        Socket socket = new Socket(host, port);
        System.out.println("🟢 Conectado al servidor: " + host + ":" + port);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // autoflush significa que no hay que hacer flush manual ¿Que es el flush? Pues vaciar el buffer de salida

        // Lanzamos un hilo para leer los mensajes que nos envía el servidor
        Thread messageThread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    logger.debug("Mensaje recibido: " + message);
                    System.out.println(message);
                }
            } catch (Exception e) {
                logger.error("Error en el cliente: " + e.getMessage());
            }
        });
        messageThread.start();

        // Leemos los mensajes del usuario y los enviamos al servidor
        Scanner scanner = new Scanner(System.in);
        String inputLine;
        while ((inputLine = scanner.nextLine()) != null) {
            logger.debug("Mensaje enviado: " + inputLine);
            // si es SALIR salimos
            if (inputLine.equals("SALIR")) {
                out.println("¡Adiós!");
                break;
            }
            out.println(inputLine);
        }
        logger.debug("Cerrando cliente");
        System.out.println("Cerrando cliente");
        socket.close();
    }
}