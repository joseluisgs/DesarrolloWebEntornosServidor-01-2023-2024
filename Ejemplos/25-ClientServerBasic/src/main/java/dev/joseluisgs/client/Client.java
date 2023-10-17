package dev.joseluisgs.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {

            // Abrir
            Socket socket = new Socket("localhost", 3000);

            // Tomo los stream de entrada y salida
            var outStream = socket.getOutputStream();
            var inStream = socket.getInputStream();

            // Procesar
            // Esto lo hago porque quiero enviar un mensaje de texto
            PrintWriter out = new PrintWriter(outStream, true);
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

            // Aquí puedes cambiar los mensajes que se envían al servidor
            String[] mensajes = {"login", "fecha", "uuid", "otro", "salir"};

            // Vamos a mandar todos los mensajes
            for (String mensaje : mensajes) {
                // Escribimos en la salida
                out.println(mensaje);
                // Leemos la respuesta (se bloquea hasta que llegue)
                String response = in.readLine();
                // Imprimimos la respuesta
                System.out.println("Respuesta del servidor: " + response);
                Thread.sleep(2000);
            }

            // Cerrar
            // Es importante cerrar los streams y el socket
            in.close();
            out.close();
            socket.close();
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}