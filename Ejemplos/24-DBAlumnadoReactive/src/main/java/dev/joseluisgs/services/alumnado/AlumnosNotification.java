package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.models.Notificacion;
import reactor.core.publisher.Flux;

public interface AlumnosNotification {
    Flux<Notificacion<Alumno>> getNotificationAsFlux();

    void notify(Notificacion<Alumno> notificacion);
}
