package dev.joseluisgs.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.client.exceptions.ClientException;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static dev.joseluisgs.common.models.Request.Type.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 3000;
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private final Gson gson;
    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String token;

    public Client() {
        gson = new Gson();
    }

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

            Thread.sleep(1000); // Esperamos 15 segundos para que caduque el token y vemos que pasa

            sendRequestFechaToken(token); // Usamos el token para pedir la fecha (no se necesitará el rol)

            sendRequestUuidWithToken(token); // Además de token debemos ser admin, cambia

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
        Request<Login> request = new Request<>(LOGIN, new Login("pepe", "pepe1234"), null, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + LOGIN);
        logger.debug("Petición enviada: " + request);
        // Enviamos la petición
        out.println(gson.toJson(request));


        // Recibimos la respuesta
        try {
            // Es estring porque el content es String no datos
            Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
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

    private void sendRequestFechaToken(String token) throws ClientException, IOException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(FECHA, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + FECHA);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> System.out.println("🟢 La hora del sistema es: " + response.content());
            default -> throw new ClientException(response.content());
        }

    }

    private void sendRequestUuidWithToken(String token) throws ClientException, IOException {

        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(UUID, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + UUID);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
        Response<String> response = gson.fromJson(in.readLine(), new TypeToken<Response<String>>() {
        }.getType());
        logger.debug("Respuesta recibida: " + response.toString());
        // Ahora podríamos implementar un switch para cada tipo de respuesta
        // y hacer lo que queramos con ella...
        System.out.println("Respuesta recibida de tipo: " + response.status());

        switch (response.status()) {
            case OK -> System.out.println("🟢 El uuid solicitado es: " + response.content());
            default -> throw new ClientException(response.content());
        }
    }


    private void sendRequestSalir() throws IOException, ClientException {
        // Al usar el toString me ahorro el problem ade las fechas con Gson
        Request<String> request = new Request<>(SALIR, null, token, LocalDateTime.now().toString());
        System.out.println("Petición enviada de tipo: " + SALIR);
        logger.debug("Petición enviada: " + request);

        // Enviamos la petición
        out.println(gson.toJson(request));

        // Recibimos la respuesta
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