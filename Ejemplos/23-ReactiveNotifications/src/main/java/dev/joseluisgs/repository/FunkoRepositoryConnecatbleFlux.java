package dev.joseluisgs.repository;

import dev.joseluisgs.models.Funko;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
Por favor, ten en cuenta que ConnectableFlux comienza a emitir elementos tan pronto como
 se llama a connect(), independientemente de si tiene suscriptores o no.
 Esto significa que si se emiten elementos antes de que se suscriba ningún suscriptor,
 estos elementos se perderán. Si esto es un problema en tu caso,
 es posible que debas considerar otras soluciones, como almacenar los elementos
 emitidos en un buffer hasta que se suscriba un suscriptor.

 En resumen, la principal diferencia entre usar un ConnectableFlux<Notificacion> y
 un Flux<Notificacion> con share() es que el primero requiere
 que llames explícitamente a connect() antes de que comience a emitir elementos,
 mientras que el segundo comenzará a emitir elementos tan pronto como tenga
 al menos un suscriptor.
 Ambos compartirán las emisiones con todos sus suscriptores.
 */

public class FunkoRepositoryConnecatbleFlux {
    private final List<Funko> funkos = new ArrayList<>();

    private final Flux<List<Funko>> funkoFlux;
    private final Flux<String> funkoNotificationFlux;
    private FluxSink<List<Funko>> funkoFluxSink;
    private FluxSink<String> funkoNotification;

    public FunkoRepositoryConnecatbleFlux() {
        ConnectableFlux<List<Funko>> connectableFunkoFlux = Flux.<List<Funko>>create(emitter -> this.funkoFluxSink = emitter).publish();
        ConnectableFlux<String> connectableFunkoNotificationFlux = Flux.<String>create(emitter -> this.funkoNotification = emitter).publish();

        funkoFlux = connectableFunkoFlux;
        funkoNotificationFlux = connectableFunkoNotificationFlux;

        connectableFunkoFlux.connect();
        connectableFunkoNotificationFlux.connect();
    }

    public void add(Funko funko) {
        funkos.add(funko);
        funkoFluxSink.next(funkos);
        funkoNotification.next("Se ha añadido un nuevo Funko: " + funko);
    }

    public void delete(UUID id) {
        Optional<Funko> funkoToRemove = funkos.stream().filter(f -> f.getId().equals(id)).findFirst();
        funkoToRemove.ifPresent(f -> {
            funkos.remove(f);
            funkoFluxSink.next(funkos);
            funkoNotification.next("Se ha eliminado un Funko: " + f);
        });
    }

    public Flux<List<Funko>> getAllAsFlux() {
        return funkoFlux;
    }

    public Flux<String> getNotificationAsFlux() {
        return funkoNotificationFlux;
    }
}
