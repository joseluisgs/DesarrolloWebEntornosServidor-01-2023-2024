package dev.joseluisgs.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.common.Login;
import dev.joseluisgs.common.Request;
import dev.joseluisgs.common.Response;
import dev.joseluisgs.common.User;
import dev.joseluisgs.server.repositories.UsersRepository;
import dev.joseluisgs.server.services.TokenService;
import org.mindrot.jbcrypt.BCrypt;
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
            Request request; // Cuiudado con el tipo de la petición, haz el cast al procesarla

            // Cuidado con lo de salir!!!!
            while (true) {
                clientInput = in.readLine();
                logger.debug("Petición recibida en bruto: " + clientInput);
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

    @SuppressWarnings("unchecked")
    private void handleRequest(Request<?> request) throws IOException {
        logger.debug("Petición para procesar: " + request);
        // Procesamos la petición y devolvemos la respuesta, esto puede ser un método
        switch (request.type()) {
            case LOGIN -> processLogin((Request<Login>) request);
            case FECHA -> processFecha((Request<String>) request);
            case UUID -> processUuid((Request<String>) request);
            case SALIR -> processSalir();
            default ->
                    out.println(gson.toJson(new Response<>(Response.Status.ERROR, "No tengo ni idea", LocalDateTime.now().toString())));
        }
    }

    private void processFecha(Request<String> request) {
        logger.debug("Petición de fecha recibida: " + request);
        // Para la fecha solo vamos a comporbar que el token esté activo
        // Si no lo está, no se procesa la petición
        var token = request.token();
        if (TokenService.getInstance().verifyToken(token, Server.TOKEN_SECRET)) {
            logger.debug("Token válido");
            out.println(gson.toJson(new Response<>(Response.Status.OK, LocalDateTime.now().toString(), LocalDateTime.now().toString())));
        } else {
            logger.warn("Token no válido");
            out.println(gson.toJson(new Response<>(Response.Status.ERROR, "Token no válido o caducado", LocalDateTime.now().toString())));
        }
    }

    private void processUuid(Request<String> request) {
        logger.debug("Petición de UUID recibida: " + request);
        // Para el UUID solo vamos a comporbar que el token esté activo
        // y que el usuario sea admin
        var token = request.token();
        if (TokenService.getInstance().verifyToken(token, Server.TOKEN_SECRET)) {
            logger.debug("Token válido");
            var claims = TokenService.getInstance().getClaims(token, Server.TOKEN_SECRET);
            var id = claims.get("userid").asInt(); // es verdad que podríamos obtener otro tipo de datos
            var user = UsersRepository.getInstance().findByById(id);
            if (user.isPresent() && user.get().role().equals(User.Role.ADMIN)) {
                logger.debug("Usuario válido y admin procesamos la petición");
                out.println(gson.toJson(new Response<>(Response.Status.OK, UUID.randomUUID().toString(), LocalDateTime.now().toString())));
            } else {
                logger.warn("Usuario no válido");
                out.println(gson.toJson(new Response<>(Response.Status.ERROR, "Usuario no válido o no tiene permisos", LocalDateTime.now().toString())));
            }

        } else {
            logger.warn("Token no válido");
            out.println(gson.toJson(new Response<>(Response.Status.ERROR, "Token no válido o caducado", LocalDateTime.now().toString())));
        }
    }

    private void processSalir() throws IOException {
        out.println(gson.toJson(new Response<>(Response.Status.BYE, "Adios", LocalDateTime.now().toString())));
        closeConnection();
    }

    // Aqui es muy importante que digas el tipo porque si lo vamos a usar
    private void processLogin(Request<Login> request) {
        logger.debug("Petición de login recibida: " + request);
        // Aquí procesamos el login es un dato anidado!!! Descomponemos la petición
        Login login = gson.fromJson(String.valueOf(request.content()), new TypeToken<Login>() {
        }.getType());
        // existe el usuario??
        // System.out.println(login);
        var user = UsersRepository.getInstance().findByByUsername(login.username());
        if (user.isPresent()) {
            var canLogin = BCrypt.checkpw(login.password(), user.get().password());
            if (canLogin) {
                // Creamos el token
                var token = TokenService.getInstance().createToken(user.get(), Server.TOKEN_SECRET, Server.TOKEN_EXPIRATION);
                // Enviamos la respuesta
                logger.debug("Respuesta enviada: " + token);
                out.println(gson.toJson(new Response<>(Response.Status.TOKEN, token, LocalDateTime.now().toString())));
            } else {
                logger.warn("Usuario o contraseña incorrectos");
                out.println(gson.toJson(new Response<>(Response.Status.ERROR, "Usuario o contraseña incorrectos", LocalDateTime.now().toString())));
            }
        } else {
            logger.warn("Usuario no encontrado");
            out.println(gson.toJson(new Response<>(Response.Status.ERROR, "Usuario no encontrado", LocalDateTime.now().toString())));
        }

    }
}
