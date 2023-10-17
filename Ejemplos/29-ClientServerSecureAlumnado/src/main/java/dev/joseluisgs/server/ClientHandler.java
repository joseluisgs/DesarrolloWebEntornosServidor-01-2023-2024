package dev.joseluisgs.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.common.models.*;
import dev.joseluisgs.server.exceptions.server.ServerException;
import dev.joseluisgs.server.repositories.users.UsersRepository;
import dev.joseluisgs.server.services.services.alumnado.AlumnosService;
import dev.joseluisgs.server.services.token.TokenService;
import dev.joseluisgs.utils.LocalDateTimeAdapter;
import dev.joseluisgs.utils.UuidAdapter;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ClientHandler extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final Gson gson = new GsonBuilder()
            //.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(UUID.class, new UuidAdapter()).create();
    private final long clientNumber;
    private final AlumnosService allumnosService;
    BufferedReader in;
    PrintWriter out;

    public ClientHandler(Socket socket, long clientNumber, AlumnosService alumnosService) {
        this.clientSocket = socket;
        this.clientNumber = clientNumber;
        this.allumnosService = alumnosService;
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
        } catch (ServerException ex) {
            // Concentramos todos los errores aquí
            out.println(gson.toJson(new Response(Response.Status.ERROR, ex.getMessage(), LocalDateTime.now().toString())));
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
    private void handleRequest(Request request) throws IOException, ServerException {
        logger.debug("Petición para procesar: " + request);
        // Procesamos la petición y devolvemos la respuesta, esto puede ser un método
        switch (request.type()) {
            case LOGIN -> processLogin(request);
            case SALIR -> processSalir();
            case GETALL -> procesasGetAll(request);
            case GETBYID -> procesasGetById(request);
            case GETBYUUID -> procesasGetByUuid(request);
            case POST -> procesarPost(request);
            case UPDATE -> procesasUpdate(request);
            case DELETE -> procesasDelete(request);
            default -> new Response(Response.Status.ERROR, "No tengo ni idea", LocalDateTime.now().toString());
        }
    }

    private void processSalir() throws IOException {
        out.println(gson.toJson(new Response(Response.Status.BYE, "Adios", LocalDateTime.now().toString())));
        closeConnection();
    }

    // Aqui es muy importante que digas el tipo porque si lo vamos a usar
    private void processLogin(Request request) throws ServerException {
        logger.debug("Petición de login recibida: " + request);
        // Aquí procesamos el login es un dato anidado!!! Descomponemos la petición
        Login login = gson.fromJson(String.valueOf(request.content()), new TypeToken<Login>() {
        }.getType());
        // existe el usuario??
        // System.out.println(login);
        var user = UsersRepository.getInstance().findByByUsername(login.username());
        if (user.isEmpty() || !BCrypt.checkpw(login.password(), user.get().password())) {
            logger.warn("Usuario no encontrado o falla la contraseña");
            throw new ServerException("Usuario o contraseña incorrectos");
        }
        // Creamos el token
        var token = TokenService.getInstance().createToken(user.get(), Server.TOKEN_SECRET, Server.TOKEN_EXPIRATION);
        // Enviamos la respuesta
        logger.debug("Respuesta enviada: " + token);
        out.println(gson.toJson(new Response(Response.Status.TOKEN, token, LocalDateTime.now().toString())));

    }

    private Optional<User> procesarToken(String token) throws ServerException {
        if (TokenService.getInstance().verifyToken(token, Server.TOKEN_SECRET)) {
            logger.debug("Token válido");
            var claims = TokenService.getInstance().getClaims(token, Server.TOKEN_SECRET);
            var id = claims.get("userid").asInt(); // es verdad que podríamos obtener otro tipo de datos
            var user = UsersRepository.getInstance().findByById(id);
            if (user.isEmpty()) {
                logger.error("Usuario no autenticado correctamente");
                throw new ServerException("Usuario no autenticado correctamente");
            }
            return user;
        } else {
            logger.error("Token no válido");
            throw new ServerException("Token no válido");
        }
    }

    private void procesasGetAll(Request request) throws ServerException {
        procesarToken(request.token());
        // Todos pueden obtener todos los alumnos
        allumnosService.findAll()
                .collectList()
                .subscribe(alumnos -> {
                    logger.debug("Respuesta enviada: " + alumnos);
                    var resJson = gson.toJson(alumnos); // contenido
                    out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString()))); // Respuesta
                });
    }

    private void procesasGetById(Request request) throws ServerException {
        procesarToken(request.token());
        // pasar string to long
        var myId = Long.parseLong(request.content());
        allumnosService.findById(myId).subscribe(
                alumno -> {
                    logger.debug("Respuesta enviada: " + alumno);
                    var resJson = gson.toJson(alumno); // Contenido
                    out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString()))); // Respuesta
                },
                error -> {
                    logger.warn("Alumno no encontrado con id: " + request.content());
                    out.println(gson.toJson(new Response(Response.Status.ERROR, error.getMessage(), LocalDateTime.now().toString())));
                }
        );
    }

    private void procesasGetByUuid(Request request) throws ServerException {
        procesarToken(request.token());
        // pasar string to uuid
        var myId = UUID.fromString(request.content());
        allumnosService.findByUuid(myId).subscribe(
                alumno -> {
                    logger.debug("Respuesta enviada: " + alumno);
                    var resJson = gson.toJson(alumno); // contenido
                    out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString()))); // Respuesta
                },
                error -> {
                    logger.error("Alumno no encontrado con uuid: " + request.content());
                    out.println(gson.toJson(new Response(Response.Status.ERROR, error.getMessage(), LocalDateTime.now().toString())));
                }
        );

    }


    private void procesarPost(Request request) throws ServerException {
        var user = procesarToken(request.token());
        if (user.isPresent() && user.get().role().equals(User.Role.ADMIN)) { // Solo los admin pueden crear
            Alumno alumnoToSave = gson.fromJson(String.valueOf(request.content()), new TypeToken<Alumno>() {
            }.getType());
            allumnosService.save(alumnoToSave).subscribe(
                    alumno -> {
                        logger.debug("Respuesta enviada: " + alumno);
                        var resJson = gson.toJson(alumno); // Mandamos todo como cadenas contenido
                        out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString()))); // Respuesta
                    },
                    error -> {
                        logger.error("Alumno no encontrado con id: " + error.getMessage());
                        out.println(gson.toJson(new Response(Response.Status.ERROR, error.getMessage(), LocalDateTime.now().toString())));
                    }
            );
        } else {
            logger.error("Usuario no autenticado correctamente o no tiene permisos para esta acción");
            throw new ServerException("Usuario no autenticado correctamente o no tiene permisos para esta acción");
        }
    }

    private void procesasUpdate(Request request) throws ServerException {
        var user = procesarToken(request.token());
        if (user.isPresent() && user.get().role().equals(User.Role.ADMIN)) { // Solo los admin pueden crear
            Alumno alumnoToUpdate = gson.fromJson(String.valueOf(request.content()), new TypeToken<Alumno>() {
            }.getType());
            allumnosService.update(alumnoToUpdate).subscribe(
                    alumno -> {
                        logger.debug("Respuesta enviada: " + alumno);
                        var resJson = gson.toJson(alumno); // Mandamos todo como cadenas contenido
                        out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString()))); // Respuesta
                    },
                    error -> {
                        logger.error("Alumno no encontrado con id: " + error.getMessage());
                        out.println(gson.toJson(new Response(Response.Status.ERROR, error.getMessage(), LocalDateTime.now().toString())));
                    }
            );
        } else {
            logger.error("Usuario no autenticado correctamente o no tiene permisos para esta acción");
            throw new ServerException("Usuario no autenticado correctamente o no tiene permisos para esta acción");
        }
    }


    private void procesasDelete(Request request) throws ServerException {
        var user = procesarToken(request.token());
        if (user.isPresent() && user.get().role().equals(User.Role.ADMIN)) { // Solo los admin pueden crear
            // pasar string to long
            var myId = Long.parseLong(request.content());
            allumnosService.deleteById(myId).subscribe(
                    alumno -> {
                        var resJson = gson.toJson(alumno); // Mandamos todo como cadenas
                        out.println(gson.toJson(new Response(Response.Status.OK, resJson, LocalDateTime.now().toString())));
                    },
                    error -> {
                        logger.error("Alumno no encontrado con id: " + request.content());
                        out.println(gson.toJson(new Response(Response.Status.ERROR, error.getMessage(), LocalDateTime.now().toString())));
                    }
            );
        } else {
            logger.error("Usuario no autenticado correctamente o no tiene permisos para esta acción");
            throw new ServerException("Usuario no autenticado correctamente o no tiene permisos para esta acción");
        }

    }


}
