package dev.joseluisgs;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        long TIME = 3000L;
        System.out.println("Jugando con Futures y CompletableFutures");

        // Crea una instancia de Callable que representa la tarea, es Callable porque devuleve un resultado String
        Callable<String> callableTask = () -> {
            Thread.sleep(TIME); // Simula una tarea que tarda 4 segundos en completarse
            return "Resultado de la tarea exitoso";
        };

        System.out.println("Con Future");
        // Crea un ExecutorService con un pool de 1 hilo
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Lanza el Future utilizando el ExecutorService
        Future<String> future = executorService.submit(callableTask);

        // Espera hasta que la tarea se complete o se produzca un timeout
        try {
            // si no ha terminado imprimimos un mensaje cada 500 milisegundos
            // Comenta este while y mira si salta la excepción de Timeout
            /*while (!future.isDone()) {
                System.out.println("La tarea no ha terminado todavía...");
                Thread.sleep(500);
            }*/
            // Espera hasta que la tarea se complete o se produzca un timeout de 3 segundos
            String resultado = future.get(3, TimeUnit.SECONDS);
            System.out.println("Resultado: " + resultado);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error: la tarea ha fallado debido a una excepción " + e.getMessage());
        } catch (TimeoutException e) {
            future.cancel(true); // Cancela la tarea si ha pasado el timeout de 3 segundos
            System.err.println("Timeout: la tarea no se ha completado en 3 segundos");
        }

        // Finaliza el ExecutorService
        executorService.shutdown();

        // Ahora con CompletableFuture
        System.out.println("Con CompletableFuture");
        // Crea un CompletableFuture a partir del Callable
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return callableTask.call(); // Llama al Callable
            } catch (Exception e) {
                System.err.println("Error: la tarea ha fallado debido a una excepción " + e.getMessage());
                return null;
            }
        });

        // Espera hasta que la tarea se complete o se produzca un timeout
        try {
            // si no ha terminado imprimimos un mensaje cada 500 milisegundos
            // Comenta este while y mira si salta la excepción de Timeout
            while (!completableFuture.isDone()) {
                System.out.println("La tarea no ha terminado todavía...");
                Thread.sleep(500);
            }
            // Espera hasta que la tarea se complete o se produzca un timeout de 3 segundos
            String resultado = completableFuture.get(3, TimeUnit.SECONDS);
            System.out.println("Resultado: " + resultado);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error: la tarea ha fallado debido a una excepción " + e.getMessage());
        } catch (TimeoutException e) {
            completableFuture.cancel(true); // Cancela la tarea si ha pasado el timeout de 3 segundos
            System.err.println("Timeout: la tarea no se ha completado en 3 segundos");
        }
    }
}