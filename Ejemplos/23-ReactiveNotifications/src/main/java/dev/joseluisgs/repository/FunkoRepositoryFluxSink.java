package dev.joseluisgs.repository;

import dev.joseluisgs.models.Funko;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FunkoRepositoryFluxSink {
    private final List<Funko> funkos = new ArrayList<>();

    // una interfaz proporcionada por Reactor que
    // permite la generación programática de eventos en un Flux.
    private final Flux<List<Funko>> funkoFlux;
    private final Flux<String> funkoNotificationFlux;
    // Usando el método create toma una función lambda que se invoca con un FluxSink.
    // En este caso, la función simplemente almacena el FluxSink
    // en la variable funkoFluxSink para su uso posterior.
    // El método share se utiliza para hacer que este Flux sea "compartido",
    // lo que significa que todos los suscriptores recibirán los mismos eventos.
    private FluxSink<List<Funko>> funkoFluxSink;
    private FluxSink<String> funkoNotification;

    public FunkoRepositoryFluxSink() {
        funkoFlux = Flux.<List<Funko>>create(emitter -> this.funkoFluxSink = emitter).share();
        funkoNotificationFlux = Flux.<String>create(emitter -> this.funkoNotification = emitter).share();
    }


    public void add(Funko funko) {
        funkos.add(funko);
        funkoFluxSink.next(funkos); // Emite el evento con la lista actualizada
        funkoNotification.next("Se ha añadido un nuevo Funko: " + funko); // Emite el evento con la notificacion
    }

    public void delete(UUID id) {
        Optional<Funko> funkoToRemove = funkos.stream().filter(f -> f.getId().equals(id)).findFirst();
        funkoToRemove.ifPresent(f -> {
            funkos.remove(f);
            funkoFluxSink.next(funkos); // Emite el evento con la lista actualizada
            funkoNotification.next("Se ha eliminado un Funko: " + f); // Emite el evento con la notificacion
        });
    }

    public Flux<List<Funko>> getAllAsFlux() {
        return funkoFlux;
    }

    public Flux<String> getNotificationAsFlux() {
        return funkoNotificationFlux;
    }
}