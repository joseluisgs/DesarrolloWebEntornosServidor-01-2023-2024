package dev.joseluisgs.models;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;


/**
 * Ejemplo de uso de hilos con Kotlin
 * Thread lanza hilos, podemos heredar de Hilos, o implementar
 * Runnable: para ejecutar código en un hilo
 * Callable: para ejecutar código en un hilo y devolver un valor
 * ExecutorService: para ejecutar hilos teniendo un pool de hilos fijos
 * y con ello optimizar recursos. Es decir, no creando hilos son razon!
 */

public class Caja implements Runnable, Callable<Integer> {
    private final int caja;
    private final Cliente cliente;
    private final MonitorRecaudacion monitorRecaudacion;

    public Caja(int caja, Cliente cliente, MonitorRecaudacion monitorRecaudacion) {
        this.caja = caja;
        this.cliente = cliente;
        this.monitorRecaudacion = monitorRecaudacion;
    }

    // Es para la ejecución secuencial
    public int procesar() {
        var precioTotal = 0;
        // Vamos a medir el tiempo de ejecución
        var tiempoInicial = System.currentTimeMillis();
        System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + " que tiene " + cliente.getCarro().getProductos().size() + " productos");
        for (int i = 0; i < cliente.getCarro().getProductos().size(); i++) {
            var producto = cliente.getCarro().getProductos().get(i);
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + ", producto: " + (i + 1) + " con precio: " + producto.getPrecio() + " €");
            try {
                Thread.sleep(producto.getPrecio() * 200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Quí obtenemos el total, pero no nos daría el total de la compra
            // porque una vez que termina el hilo, se pierde el valor
            // por eso necesitamos el monitor de recaudación
            precioTotal += producto.getPrecio();
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " terminó de atender a " + cliente.getNombre());
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " precio total de " + cliente.getNombre() + ": " + precioTotal + " €");
            // Vamos a medir el tiempo de ejecución
            var tiempoFinal = System.currentTimeMillis();
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " tiempo de atención final: " + (tiempoFinal - tiempoInicial) + " ms");
        }
        return precioTotal;
    }

    // Si usamos Runnable no podemos devolver un valor
    // Es por eso que si queremos almacenar e intercambiar valores entre hilos
    // debemos usar un monitor para compartir recursos con acceso concurrente
    @Override
    public void run() {
        var precioTotal = 0;
        // Vamos a medir el tiempo de ejecución
        var tiempoInicial = System.currentTimeMillis();
        System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + " que tiene " + cliente.getCarro().getProductos().size() + " productos");
        for (int i = 0; i < cliente.getCarro().getProductos().size(); i++) {
            var producto = cliente.getCarro().getProductos().get(i);
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + ", producto: " + (i + 1) + " con precio: " + producto.getPrecio() + " €");
            try {
                Thread.sleep(producto.getPrecio() * 200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Quí obtenemos el total, pero no nos daría el total de la compra
            // porque una vez que termina el hilo, se pierde el valor
            // por eso necesitamos el monitor de recaudación
            precioTotal += producto.getPrecio();
            monitorRecaudacion.addRecaudacion(producto.getPrecio());
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " terminó de atender a " + cliente.getNombre());
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " precio total de " + cliente.getNombre() + ": " + precioTotal + " €");
            // Vamos a medir el tiempo de ejecución
            var tiempoFinal = System.currentTimeMillis();
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " tiempo de atención final: " + (tiempoFinal - tiempoInicial) + " ms");
        }
    }


    @Override
    public Integer call() {
        // Si usamos Callable podemos devolver un valor y usarlo en otro hilo
        var precioTotal = 0;
        // Vamos a medir el tiempo de ejecución
        var tiempoInicial = System.currentTimeMillis();
        System.out.println("Caja: " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + " que tiene " + cliente.getCarro().getProductos().size() + " productos");
        for (int i = 0; i < cliente.getCarro().getProductos().size(); i++) {
            var producto = cliente.getCarro().getProductos().get(i);
            System.out.println("Caja: " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + ", producto: " + (i + 1) + " con precio: " + producto.getPrecio() + " €");
            try {
                Thread.sleep(producto.getPrecio() * 200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            precioTotal += producto.getPrecio();
            System.out.println("Caja: " + Thread.currentThread().getName() + " terminó de atender a " + cliente.getNombre());
            System.out.println("Caja: " + Thread.currentThread().getName() + " precio total de " + cliente.getNombre() + ": " + precioTotal + " €");
            // Vamos a medir el tiempo de ejecución
            var tiempoFinal = System.currentTimeMillis();
            System.out.println("Caja: " + Thread.currentThread().getName() + " tiempo de atención final: " + (tiempoFinal - tiempoInicial) + " ms");
        }
        return precioTotal;
    }

    public CompletableFuture<Integer> procesarAsync() {
        return CompletableFuture.supplyAsync(() -> {
            var precioTotal = 0;
            // Vamos a medir el tiempo de ejecución
            var tiempoInicial = System.currentTimeMillis();
            System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + " que tiene " + cliente.getCarro().getProductos().size() + " productos");
            for (int i = 0; i < cliente.getCarro().getProductos().size(); i++) {
                var producto = cliente.getCarro().getProductos().get(i);
                System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " atendiendo a " + cliente.getNombre() + ", producto: " + (i + 1) + " con precio: " + producto.getPrecio() + " €");
                try {
                    Thread.sleep(producto.getPrecio() * 200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                precioTotal += producto.getPrecio();
                System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " terminó de atender a " + cliente.getNombre());
                System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " precio total de " + cliente.getNombre() + ": " + precioTotal + " €");
                // Vamos a medir el tiempo de ejecución
                var tiempoFinal = System.currentTimeMillis();
                System.out.println("Caja: " + caja + " " + Thread.currentThread().getName() + " tiempo de atención final: " + (tiempoFinal - tiempoInicial) + " ms");
            }
            return precioTotal;
        });
    }
}