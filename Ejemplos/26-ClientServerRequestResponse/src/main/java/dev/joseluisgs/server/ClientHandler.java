package dev.joseluisgs.server;

import com.google.gson.Gson;
import dev.joseluisgs.models.Request;
import dev.joseluisgs.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientHandler extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final Gson gson;
    private final long clientNumber;
    BufferedReader in;
    PrintWriter out;

    public ClientHandler(Socket socket, long clientNumber) {
        this.clientSocket = socket;
        this.gson = new Gson();
        this.clientNumber = clientNumber;
    }

    public void run() {
        try {

            openConnection();

            String clientInput;
            Request<String> request;

            // Cuidado con lo de salir!!!!
            while (true) {
                clientInput = in.readLine();
                request = gson.fromJson(clientInput, Request.class);
                handleRequest(request);
            }

        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    private void closeConnection() throws IOException {
        logger.debug("Cerrando la conexión con el cliente: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        out.close();
        in.close();
        clientSocket.close();
    }

    private void openConnection() throws IOException {
        logger.debug("Conectando con el cliente nº: " + clientNumber + " : " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private void handleRequest(Request<String> request) throws IOException {
        // Procesamos la petición y devolvemos la respuesta, esto puede ser un método
        switch (request.type()) {
            case LOGIN ->
                    out.println(gson.toJson(new Response<>(Response.Status.OK, "Bienvenido", LocalDateTime.now().toString())));
            case FECHA ->
                    out.println(gson.toJson(new Response<>(Response.Status.OK, LocalDateTime.now().toString(), LocalDateTime.now().toString())));
            case UUID ->
                    out.println(gson.toJson(new Response<>(Response.Status.OK, UUID.randomUUID().toString(), LocalDateTime.now().toString())));
            case SALIR -> {
                out.println(gson.toJson(new Response<>(Response.Status.BYE, "Adios", LocalDateTime.now().toString())));
                closeConnection();
            }
            default ->
                    out.println(gson.toJson(new Response<>(Response.Status.ERROR, "No tengo ni idea", LocalDateTime.now().toString())));
        }
    }
}
