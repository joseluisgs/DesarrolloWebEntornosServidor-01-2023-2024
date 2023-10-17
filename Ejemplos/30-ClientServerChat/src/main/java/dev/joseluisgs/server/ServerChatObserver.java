package dev.joseluisgs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerChatObserver {
    private final Map<Integer, Socket> clients;
    private final Logger logger = LoggerFactory.getLogger(ServerChatObserver.class);
    private final int PORT = 3000;

    public ServerChatObserver() {
        logger.debug("Inicializando servidor");
        clients = new HashMap<>();
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

            // Lee los mensajes del cliente y los envía a todos los demás clientes
            String input;
            while ((input = in.readLine()) != null) {
                logger.debug("Mensaje recibido del cliente " + clientId + ": " + input);
                broadcastMessage("Cliente " + clientId + ": " + input, clientId);
            }
        } catch (Exception e) {
            logger.error("Error in client handler for client " + clientId, e);
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                logger.error("Error in client handler for client " + clientId, e);
            }
        }
    }

    // El problema de usar un patrón observer de este tipo es si tenemos muchísimos clientes,
    // ya que debemos recorrer el mapa de clientes y enviar el mensaje a cada uno de ellos.
    private synchronized void broadcastMessage(String message, int senderId) {
        for (Map.Entry<Integer, Socket> entry : clients.entrySet()) {
            if (entry.getKey() != senderId) {
                try {
                    PrintWriter out = new PrintWriter(entry.getValue().getOutputStream(), true);
                    logger.debug("Enviando mensaje al cliente " + senderId + ": " + message);
                    out.println(message);
                } catch (IOException e) {
                    logger.error("Error al enviar mensaje a: " + entry.getKey(), e);
                }
            }
        }
    }
}
