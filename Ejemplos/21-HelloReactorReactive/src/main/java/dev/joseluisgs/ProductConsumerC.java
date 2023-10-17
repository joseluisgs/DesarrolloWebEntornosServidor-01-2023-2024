package dev.joseluisgs;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class ProductConsumerC {
    public static void main(String[] args) throws InterruptedException {

        // Primer Flux
        Flux<Integer> randomNumbersFlux = generateRandomNumbers();

        // Primer suscriptor: simplemente imprime los números tal como los recibe
        randomNumbersFlux
                .doOnNext(value -> System.out.println("Suscriptor 1 va a recibir: " + value))
                .filter(x -> x % 2 == 0)
                .take(5)
                .subscribe(
                        value -> System.out.println("Suscriptor 1 consumió: " + value),
                        error -> System.err.println("Se ha producido un error: " + error),
                        () -> System.out.println("Suscriptor 1 completado")
                );


        Thread.sleep(2000);
        // Segundo suscriptor: multiplica los números por 2 antes de imprimirlos
        randomNumbersFlux
                .doOnNext(value -> System.out.println("Suscriptor 2 va a recibir: " + value))
                .map(value -> value * 2)
                .take(7)
                .subscribe(
                        value -> System.out.println("Suscriptor 2 consumió: " + value),
                        error -> System.err.println("Se ha producido un error: " + error),
                        () -> System.out.println("Suscriptor 2 completado")
                );

        // Espera hasta que se completen todos los elementos del flujo.
        // Esto es necesario porque la generación de números aleatorios es asincrónica.
        randomNumbersFlux.blockLast();
    }

    private static Flux<Integer> generateRandomNumbers() {
        Random rand = new Random();

        return Flux.interval(Duration.ofMillis(500), Duration.ofMillis(1500))
                // Transforma los valores del intervalo en números aleatorios entre 1 y 100
                .map(i -> rand.nextInt(100) + 1).take(50);
    }
}
