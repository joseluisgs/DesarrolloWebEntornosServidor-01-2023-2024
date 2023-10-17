package dev.joseluisgs;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class ProductConsumerA {
    public static void main(String[] args) throws InterruptedException {
        // Producimos valores constantes cada segundo... son infinitos
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1));

        // Consumimos los valores pares producidos
        intervalFlux
                .filter(x -> x % 2 == 0)
                .map(x -> x * 10)
                .take(10) // toma al menos al menos x Valores (usa take) o hasta que se complete (usa blockLast)
                .subscribe(
                        value -> System.out.println("Consumido: " + value), // Se ejecuta cada vez que llega un valor
                        error -> System.err.println("Se ha producido un error: " + error), // Se ejecuta cuando se produce un error
                        () -> System.out.println("Completado") // Se ejecuta cuando se completa el flujo (no es obligatorio)
                );

        // Mantén el hilo principal vivo durante un tiempo para que pueda consumir los valores
        // Thread.sleep(10000);
        intervalFlux.blockLast(); // como no termina nunca, bloqueamos el hilo principal
        // toma al menos al menos x Valores (usa take) o hasta que se complete (usa blockLast)
    }
}
