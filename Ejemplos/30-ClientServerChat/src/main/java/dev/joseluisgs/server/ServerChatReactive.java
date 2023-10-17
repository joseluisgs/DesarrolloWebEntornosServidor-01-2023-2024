package dev.joseluisgs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerChatReactive {
    private final Map<Integer, Socket> clients;
    private final Sinks.Many<String> messageSink;
    private final Logger logger = LoggerFactory.getLogger(ServerChatReactive.class);
    private final int PORT = 3000;

    public ServerChatReactive() {
        logger.debug("Inicializando servidor");
        clients = new HashMap<>();
        messageSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public static void main(String[] args) throws Exception {
        ServerChatReactive serverChatReactive = new ServerChatReactive();
        serverChatReactive.start();
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(this.PORT);
        System.out.println("🟢 Servidor de chat escuchando en el puerto: " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            int clientId = assignClientId();
            clients.put(clientId, clientSocket);

            // Creo el nuevo hilo y lo inicio
            logger.info("Nuevo cliente conectado con id: " + clientId);
            Thread clientThread = new Thread(() -> {
                try {
                    clientHandler(clientId, clientSocket);
                } finally {
                    logger.info("Cliente " + clientId + " desconectado");
                    clients.remove(clientId); // Eliminamos el cliente del mapa
                }
            });
            clientThread.start(); // Iniciamos el hilo
        }
    }

    private synchronized int assignClientId() {
        return clients.size() + 1;
    }

    private void clientHandler(int clientId, Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Envía el ID del cliente al cliente
            out.println("Cliente ID: " + clientId);

            // Crea un Flux a partir del Sinks.Many para recibir mensajes
            Flux<String> messageFlux = messageSink.asFlux()
                    .filter(msg -> !msg.startsWith("Cliente ID: " + clientId)); // Filtra los mensajes propios del cliente

            // Escucha los mensajes entrantes y los envia a todos los clientes
            // Ahora no debemos prepcuparnos por el número de clientes
            // se hace el backpressure de forma automática
            messageFlux.subscribe(msg -> {
                logger.debug("Enviando mensaje al cliente " + clientId + ": " + msg);
                out.println(msg);
            });

            // Lee los mensajes del cliente y los envía a todos los demás clientes
            String input;
            while ((input = in.readLine()) != null) {
                logger.debug("Mensaje recibido del cliente " + clientId + ": " + input);
                messageSink.tryEmitNext("Cliente " + clientId + ": " + input);
            }
        } catch (Exception e) {
            logger.error("Error in client handler for client " + clientId, e);
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                logger.error("Error al cerrar el cliente: " + clientId, e);
            }
        }
    }
}