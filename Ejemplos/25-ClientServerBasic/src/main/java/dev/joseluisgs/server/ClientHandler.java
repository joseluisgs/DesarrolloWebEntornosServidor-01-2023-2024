package dev.joseluisgs.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final InputStream inStream;
    private final OutputStreamWriter outStream;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.inStream = socket.getInputStream();
            this.outStream = new OutputStreamWriter(socket.getOutputStream());

            System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());


        } catch (IOException e) {
            throw new RuntimeException("Error al crear los streams de entrada y salida");
        }
    }

    public void run() {
        try {
            // Esto lo hacemos porque envío cosas en texto
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            PrintWriter out = new PrintWriter(outStream, true);

            String clientInput;
            // Nos ponemos a escuchar hasta que nos llegue salir
            while (!(clientInput = in.readLine()).equals("salir")) {
                switch (clientInput.toLowerCase()) {
                    case "fecha":
                        out.println(LocalDateTime.now());
                        break;
                    case "uuid":
                        out.println(UUID.randomUUID());
                        break;
                    case "login":
                        out.println("Prueba Cliente Servidor");
                        break;
                    default:
                        out.println("No tengo ni idea");
                        break;
                }
            }

            // Esto es que nos han dicho salir
            out.println("Adios!");

            // Cerramos los streams y el socket
            out.close();
            in.close();
            clientSocket.close();
            System.out.println("Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress());

        } catch (IOException e) {
            System.out.println("Error al escuchar al cliente: " + e.getMessage());
        }
    }
}