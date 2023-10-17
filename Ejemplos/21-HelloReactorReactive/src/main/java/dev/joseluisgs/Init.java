package dev.joseluisgs;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Init {
    public static void main(String[] args) {
        Mono<String> mono = Mono.just("Hello, World"); // Fuente de datos,just pasa el valor
        System.out.println("Mono");
        mono.subscribe(System.out::println); // Observador, cuando cambie actúo

        System.out.println("Flux");
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5); // fuente de datos
        Flux<Integer> transformedFlux = flux.map(n -> n * 2); // transformo, puedo hacerlo en el original
        transformedFlux.filter(x -> x % 2 != 1) // filtro
                .takeLast(2)
                .subscribe(System.out::println); // Observador que actúa


        System.out.println("Flux con error");
        var flux2 = Flux.just("1", "2", "oops", "4", "5")
                .map(i -> {
                    try {
                        return Integer.parseInt(i);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Error al parsear el número", e);
                    }
                })
                .onErrorReturn(-1);
        flux2.subscribe(
                System.out::println,
                error -> System.err.println("Se ha producido un error: " + error)
        );
    }
}