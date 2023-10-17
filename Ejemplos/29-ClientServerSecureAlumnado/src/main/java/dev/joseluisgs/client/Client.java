package dev.joseluisgs.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.client.exceptions.ClientException;
import dev.joseluisgs.common.models.Alumno;
import dev.joseluisgs.common.models.Login;
import dev.joseluisgs.common.models.Request;
import dev.joseluisgs.common.models.Response;
import dev.joseluisgs.common.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.joseluisgs.common.models.Request.Type.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new dev.joseluisgs.utils.LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new dev.joseluisgs.utils.LocalDateTimeAdapter())
            .registerTypeAdapter(java.util.UUID.class, new dev.joseluisgs.utils.UuidAdapter()).create();
    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String token;


    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.start();
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public void start() throws IOException {
        try {

            // Nos conectamos al servidor
            openConnection();

            // Probamos las peticiones, lo ideal es hacerse una y sus métodos para enviar y procesar respuesta
            token = sendRequestLogin(); // Nos devolverá el token, pon datos incorrectos

            Thread.sleep(150); // Esperamos 15 segundos para que caduque el token y vemos que pasa

            // sendRequestFechaToken(token); // Usamos el token para pedir la fecha (no se necesitará el rol)

            // sendRequestUuidWithToken(token); // Además de token debemos ser admin, cambia

            sedRequestGetAllAlumnos(token);

            var alumno = Alumno.builder().id(1L).nombre("Pepe").calificacion(9.0).build();

            sendRequestPostAlumno(token, alumno);

            sendRequestGetAlumnoById(token, "1");

            sendRequestGetAlumnoByUuid(token, alumno.getUuid().toString());

            sendRequestPostAlumno(token, Alumno.builder().id(0L).nombre("Ana").calificacion(8.0).build());
            sendRequestPostAlumno(token, Alumno.builder().id(0L).nombre("Luis").calificacion(4.0).build());
            sendRequestPostAlumno(token, Alumno.builder().id(0L).nombre("Pedro").calificacion(7.0).build());
            sendRequestPostAlumno(token, Alumno.builder().id(0L).nombre("Sara").calificacion(9.0).build());

            sedRequestGetAllAlumnos(token);

            alumno = Alumno.builder().id(1L).nombre("Updated").calificacion(10.0).build();

            sendRequestPutAlumno(token, alumno);

            sendRequestDeleteAlumno(token, "1");

            sedRequestGetAllAlumnos(token);

            sendRequestPutAlumno(token, alumno); // No existe
            sendRequestDeleteAlumno(token, "1"); // No existe

            sedRequestGetAllAlumnos(token);

            sendRequestSalir(); // Salimos (puede haber peticiones sin token si queremos o permisos

        } catch (ClientException ex) {
            logger.error("Error: " + ex.getMessage());
            System.err.println("🔴 Error: " + ex.getMessage());
            closeConnection();
            System.exit(1);
        } catch (InterruptedException e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    private void closeConnection() throws IOException {
        logger.debug("Cerrando la conexión con el servidor: " + HOST + ":" + PORT);
        System.out.println("🔵 Cerrando Cliente");
        if (in != null)
            in.close();
        if (out != null)
            out.close();
        if (socket != null)
            socket.close();
    }

    private void openConnection() throws IOException {
        System.out.println("🔵 Iniciando Cliente");
        Map<String, String> myConfig = readConfigFile();

        logger.debug("Cargando fichero de propiedades");
        // System.setProperty("javax.net.debug", "ssl, keymanager, handshake"); // Debug
        System.setProperty("javax.net.ssl.trustStore", myConfig.get("keyFile")); // llavero cliente
        System.setProperty("javax.net.ssl.trustStorePassword", myConfig.get("keyPassword")); // clave

        SSLSocketFactory clientFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) clientFactory.createSocket(HOST, PORT);

        // Opcionalmente podemos forzar el tipo de protocolo -> Poner el mismo que el cliente
        logger.debug("Protocolos soportados: " + Arrays.toString(socket.getSupportedProtocols()));
        socket.setEnabledCipherSuites(new String[]{"TLS_AES_128_GCM_SHA256"});
        socket.setEnabledProtocols(new String[]{"TLSv1.3"});

        logger.debug("Conectando al servidor: " + HOST + ":" + PORT);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("✅ Cliente conectado a " + HOST + ":" + PORT);

        infoSession(socket);

    }

    private String sendRequestLogin() throws ClientException {
        String myToken = null;
        var loginJson = gson.toJson(new Login("pepe", "pepe1234"));
        Request request = new Request(LOGIN, loginJson, null, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + LOGIN);
        logger.debug("Petición enviada: " + request);
        // Enviamos la petición
        out.println(gson.toJson(request));


        // Recibimos la respuesta
        try {
            // Es estring porque el content es String no datos
            Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
            }.getType());

            logger.debug("Respuesta recibida: " + response.toString());
            // Ahora podríamos implementar un switch para cada tipo de respuesta
            // y hacer lo que queramos con ella...
            System.out.println("Respuesta recibida de tipo: " + response.status());

            switch (response.status()) {
                case TOKEN -> {
                    System.out.println("🟢 Mi token es: " + response.content());
                    myToken = response.content();
                }
                default -> throw new ClientException("Tipo de respuesta no esperado: " + response.content());

            }
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
        }
        return myToken;
    }

    private void sedRequestGetAllAlumnos(String token) throws ClientException, IOException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request request = new Request(GETALL, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + GETALL);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> {
                List<Alumno> responseContent = gson.fromJson(response.content(), new TypeToken<List<Alumno>>() {
                }.getType());
                System.out.println("🟢 Los alumnos son: " + responseContent);
            }
            case ERROR -> System.err.println("🔴 Error: " + response.content()); // No se ha encontrado
        }
    }

    private void sendRequestGetAlumnoById(String token, String id) throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request request = new Request(GETBYID, id, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + GETBYID);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta que puede ser de distintos tipos y contenido
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> {
                Alumno responseContent = gson.fromJson(response.content(), new TypeToken<Alumno>() {
                }.getType());
                System.out.println("🟢 El alumno solicitado es: " + responseContent);
            }
            case ERROR ->
                    System.err.println("🔴 Error: Alumno no encontrado con id: " + id + ". " + response.content()); // No se ha encontrado
            default -> throw new ClientException("Error no esperado al obtener el alumno");
        }
    }

    private void sendRequestGetAlumnoByUuid(String token, String uuid) throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request request = new Request(GETBYUUID, uuid, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + GETBYUUID);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta que puede ser de distintos tipos y contenido
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());
        switch (response.status()) {
            case OK -> {
                Alumno responseContent = gson.fromJson(response.content(), new TypeToken<Alumno>() {
                }.getType());
                System.out.println("🟢 El alumno solicitado es: " + responseContent);
            }
            case ERROR ->
                    System.err.println("🔴 Error: Alumno no encontrado con uuid: " + uuid + ". " + response.content()); // No se ha encontrado
            default -> throw new ClientException("Error no esperado al obtener el alumno");
        }
    }

    private void sendRequestPostAlumno(String token, Alumno alumno) throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        // Pasamos el alumno a json, es otra forma de hacerlo y no como el login
        var alumnoJson = gson.toJson(alumno);
        Request request = new Request(POST, alumnoJson, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + POST);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta que puede ser de distintos tipos y contenido
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> {
                Alumno responseContent = gson.fromJson(response.content(), new TypeToken<Alumno>() {
                }.getType());
                System.out.println("🟢 El alumno insertado es: " + responseContent);
            }
            case ERROR ->
                    System.err.println("🔴 Error: No se ha podido insertar el alumno: " + response.content()); // No se ha encontrado
            default -> throw new ClientException("Error no esperado al insertar el alumno");
        }
    }

    private void sendRequestPutAlumno(String token, Alumno alumno) throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        // Pasamos el alumno a json, es otra forma de hacerlo y no como el login
        var alumnoJson = gson.toJson(alumno);
        Request request = new Request(UPDATE, alumnoJson, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + UPDATE);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta que puede ser de distintos tipos y contenido
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> {
                Alumno responseContent = gson.fromJson(response.content(), new TypeToken<Alumno>() {
                }.getType());
                System.out.println("🟢 El alumno actualizado es: " + responseContent);
            }
            case ERROR ->
                    System.err.println("🔴 Error: No se ha podido actualizar el alumno: " + response.content()); // No se ha encontrado
            default -> throw new ClientException("Error no esperado al actualizar el alumno");
        }
    }

    private void sendRequestDeleteAlumno(String token, String id) throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request request = new Request(DELETE, id, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + DELETE);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta que puede ser de distintos tipos y contenido
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> {
                Alumno responseContent = gson.fromJson(response.content(), new TypeToken<Alumno>() {
                }.getType());
                System.out.println("🟢 El alumno eliminado es: " + responseContent);
            }
            case ERROR ->
                    System.err.println("🔴 Error: No se ha podido eliminar el alumno con id: " + id + ". " + response.content()); // No se ha encontrado
            default -> throw new ClientException("Error no esperado al eliminar el alumno");
        }
    }


    private void sendRequestSalir() throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request request = new Request(SALIR, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + SALIR);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        Response response = gson.fromJson(in.readLine(), new TypeToken<Response>() {
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
            default -> throw new ClientException(response.content());
        }
    }

    public Map<String, String> readConfigFile() {
        try {
            logger.debug("Leyendo el fichero de configuracion");
            PropertiesReader properties = new PropertiesReader("client.properties");

            String keyFile = properties.getProperty("keyFile");
            String keyPassword = properties.getProperty("keyPassword");

            // Comprobamos que no estén vacías
            if (keyFile.isEmpty() || keyPassword.isEmpty()) {
                throw new IllegalStateException("Hay errores al procesar el fichero de propiedades o una de ellas está vacía");
            }

            // Comprobamos el fichero de la clave
            if (!Files.exists(Path.of(keyFile))) {
                throw new FileNotFoundException("No se encuentra el fichero de la clave");
            }

            Map<String, String> configMap = new HashMap<>();
            configMap.put("keyFile", keyFile);
            configMap.put("keyPassword", keyPassword);

            return configMap;
        } catch (FileNotFoundException e) {
            logger.error("Error en clave: " + e.getLocalizedMessage());
            System.exit(1);
            return null; // Este retorno nunca se ejecutará debido a System.exit(1)
        } catch (IOException e) {
            logger.error("Error al leer el fichero de configuracion: " + e.getLocalizedMessage());
            return null;
        }
    }


    private void infoSession(SSLSocket socket) {
        logger.debug("Información de la sesión");
        System.out.println("Información de la sesión");
        try {
            SSLSession session = socket.getSession();
            System.out.println("Servidor: " + session.getPeerHost());
            System.out.println("Cifrado: " + session.getCipherSuite());
            System.out.println("Protocolo: " + session.getProtocol());
            System.out.println("Identificador:" + new BigInteger(session.getId()));
            System.out.println("Creación de la sesión: " + session.getCreationTime());
            X509Certificate certificado = (X509Certificate) session.getPeerCertificates()[0];
            System.out.println("Propietario : " + certificado.getSubjectX500Principal());
            System.out.println("Algoritmo: " + certificado.getSigAlgName());
            System.out.println("Tipo: " + certificado.getType());
            System.out.println("Número Serie: " + certificado.getSerialNumber());
            // expiración del certificado
            System.out.println("Válido hasta: " + certificado.getNotAfter());
        } catch (SSLPeerUnverifiedException ex) {
            logger.error("Error en la sesión: " + ex.getLocalizedMessage());
        }
    }

}