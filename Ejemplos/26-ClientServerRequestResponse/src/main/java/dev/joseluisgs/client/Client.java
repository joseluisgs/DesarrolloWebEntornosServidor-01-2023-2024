package dev.joseluisgs.client;

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

import static dev.joseluisgs.models.Request.Type.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    private final Gson gson;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        gson = new Gson();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try {

            // Nos conectamos al servidor
            openConnection();

            // Probamos las peticiones
            sendRequest(LOGIN);
            sendRequest(FECHA);
            sendRequest(UUID);
            sendRequest(OTRO);
            sendRequest(SALIR);
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    private void closeConnection() throws IOException {
        logger.debug("Cerrando la conexión con el servidor: " + HOST + ":" + PORT);
        in.close();
        out.close();
        socket.close();
    }

    private void openConnection() throws IOException {
        logger.debug("Conectando al servidor: " + HOST + ":" + PORT);
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void sendRequest(Request.Type type) {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(type, null, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + type);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        try {
            Response response = gson.fromJson(in.readLine(), Response.class);
            logger.debug("Respuesta recibida: " + response.toString());
            // Ahora podríamos implementar un switch para cada tipo de respuesta
            // y hacer lo que queramos con ella...
            System.out.println("Respuesta recibida de tipo: " + response.status());

            switch (response.status()) {
                case OK -> System.out.println("La respuesta es: " + response.content());
                case ERROR -> System.err.println("Error: " + response.content());
                case BYE -> {
                    System.out.println("Vamos a cerrar la conexión " + response.content());
                    closeConnection();
                }
                default -> System.out.println("He recibido algo que no se que es... " + response.content());
            }

            // Esperamos un poco para que se vea bien
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e.getMessage());
        }

    }
}