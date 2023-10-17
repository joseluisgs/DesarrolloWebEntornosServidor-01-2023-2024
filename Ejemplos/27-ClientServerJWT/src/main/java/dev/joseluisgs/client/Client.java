package dev.joseluisgs.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.common.Login;
import dev.joseluisgs.common.Request;
import dev.joseluisgs.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import static dev.joseluisgs.common.Request.Type.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    private final Gson gson;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String token;

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

            // Probamos las peticiones, lo ideal es hacerse una y sus métodos para enviar y procesar respuesta
            token = sendRequestLogin(); // Nos devolverá el token, pon datos incorrectos

            Thread.sleep(1000); // Esperamos 15 segundos para que caduque el token y vemos que pasa

            sendRequestFechaToken(token); // Usamos el token para pedir la fecha (no se necesitará el rol)

            sendRequestUuidWithToken(token); // Además de token debemos ser admin, cambia

            sendRequestSalir(); // Salimos (puede haber peticiones sin token si queremos o permisos

        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e.getMessage());
        }
    }


    private void closeConnection() throws IOException {
        logger.debug("Cerrando la conexión con el servidor: " + HOST + ":" + PORT);
        System.out.println("🔵 Cerrando Cliente");
        in.close();
        out.close();
        socket.close();
    }

    private void openConnection() throws IOException {
        System.out.println("🔵 Iniciando Cliente");
        logger.debug("Conectando al servidor: " + HOST + ":" + PORT);
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("✅ Cliente conectado a " + HOST + ":" + PORT);

    }

    private String sendRequestLogin() {
        String myToken = null;
        Request<Login> request = new Request<>(LOGIN, new Login("ana", "ana1234"), null, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + LOGIN);
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
                case TOKEN -> {
                    System.out.println("🟢 Mi token es: " + response.content());
                    myToken = response.content().toString();
                }
                default -> {
                    System.err.println("🔴 Error: Tipo de respuesta no esperado: " + response.content());
                    closeConnection();
                    System.exit(1);
                }
            }

            // Esperamos un poco para que se vea bien
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e.getMessage());
        }
        return myToken;
    }

    private void sendRequestFechaToken(String token) {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        // El request es del tipo del content!!
        Request<String> request = new Request<>(FECHA, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + FECHA);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        try {
            Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
            }.getType());
            logger.debug("Respuesta recibida: " + response.toString());
            // Ahora podríamos implementar un switch para cada tipo de respuesta
            // y hacer lo que queramos con ella...
            System.out.println("Respuesta recibida de tipo: " + response.status());

            switch (response.status()) {
                case OK -> System.out.println("🟢 La hora del sistema es: " + response.content());
                case ERROR -> System.err.println("🔴 Error: " + response.content());
                default -> {
                    System.err.println("🔴 Error: Tipo de respuesta no esperado: " + response.content());
                    closeConnection();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    private void sendRequestUuidWithToken(String token) {

        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(UUID, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + UUID);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        try {
            Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
            }.getType());
            logger.debug("Respuesta recibida: " + response.toString());
            // Ahora podríamos implementar un switch para cada tipo de respuesta
            // y hacer lo que queramos con ella...
            System.out.println("Respuesta recibida de tipo: " + response.status());

            switch (response.status()) {
                case OK -> System.out.println("🟢 El uuid solicitado es: " + response.content());
                case ERROR -> System.err.println("🔴 Error: " + response.content());
                default -> {
                    System.err.println("🔴 Error: Tipo de respuesta no esperado: " + response.content());
                    closeConnection();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
    }


    private void sendRequestSalir() {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(SALIR, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + SALIR);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        try {
            Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
            }.getType());
            logger.debug("Respuesta recibida: " + response.toString());
            // Ahora podríamos implementar un switch para cada tipo de respuesta
            // y hacer lo que queramos con ella...
            System.out.println("Respuesta recibida de tipo: " + response.status());

            switch (response.status()) {
                case ERROR -> System.err.println("🔴 Error: " + response.content());
                case BYE -> {
                    System.out.println("Vamos a cerrar la conexión " + response.content());
                    closeConnection();
                }
                default -> {
                    System.err.println("🔴 Error: Tipo de respuesta no esperado: " + response.content());
                    closeConnection();
                    System.exit(1);
                }
            }

            // Esperamos un poco para que se vea bien
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e.getMessage());
        }

    }
}