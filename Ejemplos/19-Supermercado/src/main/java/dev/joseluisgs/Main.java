package dev.joseluisgs;

import dev.joseluisgs.models.Caja;
import dev.joseluisgs.models.Cliente;
import dev.joseluisgs.models.MonitorRecaudacion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Supermercado");
        System.out.println("============");
        System.out.println();
        var TOTAL_CAJAS = 4;
        // Creamos una lista de 10 clientes
        List<Cliente> clientes = new ArrayList<>();
        List<Caja> cajas = new ArrayList<>();
        for (int i = 0; i < TOTAL_CAJAS; i++) {
            clientes.add(new Cliente("Cliente " + (i + 1)));
        }
        System.out.println("Tenemos " + clientes.size() + " clientes");
        System.out.println();

        var secuencial = ejecucionSecuencial(clientes);
        var hilos = ejecucionConcurrentePoolHilos(clientes);
        var promesas = ejecucionConcurrentePoolPromesas(clientes);
        var completableAsync = ejecucionCompletableAsync(clientes);

        System.out.println("-> Fin de la ejecución");
        System.out.println("-> Recaudación total secuencial: " + secuencial + " €");
        System.out.println("-> Recaudación total con hilos: " + hilos + " €");
        System.out.println("-> Recaudación total con futures: " + promesas + " €");
        System.out.println("-> Recaudación total con completable async: " + completableAsync + " €");
    }


    private static int ejecucionSecuencial(List<Cliente> clientes) {
        // Ejecución secuencial
        var monitorRecaudacion = new MonitorRecaudacion();
        // Creamos las cajas
        var cajas = new ArrayList<Caja>();
        for (int i = 0; i < clientes.size(); i++) {
            var caja = new Caja((i + 1), clientes.get(i), monitorRecaudacion);
            cajas.add(caja);
        }

        System.out.println("***Ejecución Secuencial***");

        // Creamos tantas cajas como clientes y le pasamos el cliente y el monitor
        // Aqí el monitor no tiene sentido... pero como lo hemos metido todo en una clase, pues hay que hacerlo
        var tiempoInicial = System.currentTimeMillis();
        var recaudacionTotal = 0;
        for (var caja : cajas) {
            recaudacionTotal += caja.procesar();
        }
        var tiempoFinal = System.currentTimeMillis();
        System.out.println("-> Fin de la ejecución secuencial");
        System.out.println("-> Recaudación total: " + recaudacionTotal + " €");
        System.out.println("-> Tiempo de ejecución: " + (tiempoFinal - tiempoInicial) + " ms");
        System.out.println();
        return recaudacionTotal;
    }

    private static int ejecucionConcurrentePoolHilos(List<Cliente> clientes) {
        // Ejecución con hilos Runable
        var monitorRecaudacion = new MonitorRecaudacion();
        var cajas = new ArrayList<Caja>();
        for (int i = 0; i < clientes.size(); i++) {
            var caja = new Caja((i + 1), clientes.get(i), monitorRecaudacion);
            cajas.add(caja);
        }

        System.out.println("***Ejecución con Hilos***");
        // Creamos el pool de hilos
        var executorService = Executors.newFixedThreadPool(clientes.size());
        var tiempoInicial = System.currentTimeMillis();
        for (var caja : cajas) {
            executorService.execute(caja);
        }

        // Cerramos el pool de hilos
        executorService.shutdown();
        // Esperamos a que terminen todos los hilos
        while (!executorService.isTerminated()) {
        }
        var tiempoFinal = System.currentTimeMillis();
        System.out.println("-> Fin de la ejecución con hilos Runnable");
        System.out.println("-> Recaudación total: " + monitorRecaudacion.getRecaudacion() + " €");
        System.out.println("-> Tiempo de ejecución: " + (tiempoFinal - tiempoInicial) + " ms");
        System.out.println();
        return monitorRecaudacion.getRecaudacion();
    }

    private static int ejecucionConcurrentePoolPromesas(List<Cliente> clientes) throws InterruptedException, ExecutionException {
        // Ejecución con Futures Callable
        var monitorRecaudacion = new MonitorRecaudacion();
        var cajas = new ArrayList<Caja>();
        for (int i = 0; i < clientes.size(); i++) {
            var caja = new Caja((i + 1), clientes.get(i), monitorRecaudacion);
            cajas.add(caja);
        }

        System.out.println("***Ejecución Futures***");
        var tiempoInicial = System.currentTimeMillis();
        var recaudacionTotal = 0;
        var executorService = Executors.newFixedThreadPool(clientes.size());

        var futures = executorService.invokeAll(cajas); // Ejecutamos todos los hilos y puedo invocasr todo a la vez, mucho más comodo
        for (var future : futures) {
            recaudacionTotal += future.get(); // Aquí esperamos a que termine cada hilo y obtenemos el valor
        }
        executorService.shutdown();
        var tiempoFinal = System.currentTimeMillis();
        System.out.println("-> Fin de la ejecución con hilos Futures y Callable");
        System.out.println("-> Recaudación total: " + recaudacionTotal + " €");
        System.out.println("-> Tiempo de ejecución: " + (tiempoFinal - tiempoInicial) + " ms");
        System.out.println();
        return recaudacionTotal;
    }

    private static int ejecucionCompletableAsync(List<Cliente> clientes) {
        // Ejecución con Futures Completable Async
        var monitorRecaudacion = new MonitorRecaudacion();
        var cajas = new ArrayList<Caja>();
        for (int i = 0; i < clientes.size(); i++) {
            var caja = new Caja((i + 1), clientes.get(i), monitorRecaudacion);
            cajas.add(caja);
        }

        System.out.println("***Ejecución con Completable Async***");
        var tiempoInicial = System.currentTimeMillis();
        var recaudacionTotal = new AtomicInteger(0);

        // ejecutamos todo a la vez el metodo completableFuture<Integer> procesarAsync()
        var futures = cajas.stream().map(Caja::procesarAsync).toArray(CompletableFuture[]::new);
        // Esperamos a que terminen todos los hilos y obtenemos el valor de cada uno de ellos
        // y lo sumamos a la recaudación total
        CompletableFuture.allOf(futures).thenRun(() -> {
            for (CompletableFuture<Integer> future : futures) {
                try {
                    recaudacionTotal.addAndGet(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            var tiempoFinal = System.currentTimeMillis();
            System.out.println("-> Fin de la ejecución con hilos Futures/Completable Async");
            System.out.println("-> Recaudación total: " + recaudacionTotal.get() + " €");
            System.out.println("-> Tiempo de ejecución: " + (tiempoFinal - tiempoInicial) + " ms");
            System.out.println();
        }).join();
        return recaudacionTotal.get();
    }

}