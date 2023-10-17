package dev.joseluisgs;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class ProductConsumerB {
    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();

        // Muchas veces no sabes cuando te llegará el dato, solo reaccionas al mismo
        Flux<Integer> randomNumbersFlux = Flux.interval(Duration.ofMillis(500), Duration.ofMillis(1500))
                .map(i -> (rand.nextInt(100) + 1)).take(50);
        // Transforma los valores del intervalo en números aleatorios entre 1 y 100

        randomNumbersFlux

                .take(10)  // Limita el flujo a los primeros 10 elementos
                .subscribe(
                        value -> System.out.println("Consumido: " + value),
                        error -> System.err.println("Se ha producido un error: " + error),
                        () -> System.out.println("Completado")
                );

        // Espera hasta que se completen todos los elementos del flujo.
        // Esto es necesario porque la generación de números aleatorios es asincrónica.
        randomNumbersFlux.blockLast();
    }
}
