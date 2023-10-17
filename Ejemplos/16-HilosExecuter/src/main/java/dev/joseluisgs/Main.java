package dev.joseluisgs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Jugando con Hilos");

        System.out.println("Hola desde el hilo principal " + Thread.currentThread().getName());

        // Puedo lanzar hilo de distintas formas
        // 1. Creando una clase que herede de Thread
        MiClaseHilo hilo = new MiClaseHilo();
        hilo.start();

        // 2. Creando una clase que implemente Runnable y pasandosela a un hilo
        MiClaseRunnable runnable = new MiClaseRunnable();
        Thread hilo2 = new Thread(runnable);
        hilo2.start();
        
        // 3. Creando una clase con un lambda
        Thread hilo3 = new Thread(() -> {
            System.out.println("Hola desde la clase con lambda " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
            }
        });
        hilo3.start();

        // 4. Creando un runnable con un lambda
        Runnable runnable2 = () -> {
            System.out.println("Hola desde la clase con lambda " + Thread.currentThread().getName());
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
            }
        };
        Thread hilo4 = new Thread(runnable2);
        hilo4.start();

        // Si no ponemos los join, el hilo principal termina antes que los demás
        // prueba a comentarlos y verás
        try {
            hilo.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
        } catch (InterruptedException ex) {
            System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
        }

        // Te habras dado cuenta que el codigo no es secuencial, sino que se ejecuta en paralelo
        // por lo que el tiempo de ejecución es menor y es el tiempo del hilo que más tarde

        System.out.println("Ha terminado " + Thread.currentThread().getName());

        System.out.println("Jugando con Pool de Hilos");
        // Crea un ExecutorService con un pool de 4 hilos, y ejecuta elementos en hilos
        // Va procesando los hilos según se van liberando hasta que acaba el pool
        // Se les puede pasar un runnable o Thread
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Le vamos asignando tareas
        executor.submit(hilo); // Ejecuta el hilo
        executor.submit(hilo2); // Ejecuta el hilo
        executor.submit(hilo3); // Ejecuta el hilo
        executor.submit(hilo4); // Ejecuta el hilo

        // Ahora le pasamos otras, que irán entrado a la vez que se van liberando hilos para ejecutarlas
        // Envía 10 tareas al ExecutorService
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println("Tarea " + taskId + " ejecutada por " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
                }
            });
        }

        // Cerramos el ExecutorService, es decir no se aceptan más tareas y se terminan las que quedan
        executor.shutdown();

        // Fin del programa
        System.out.println("ADIOS");

    }
}