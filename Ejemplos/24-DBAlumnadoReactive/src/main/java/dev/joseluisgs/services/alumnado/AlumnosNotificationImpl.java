package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.models.Notificacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class AlumnosNotificationImpl implements AlumnosNotification {
    private static AlumnosNotificationImpl INSTANCE = new AlumnosNotificationImpl();

    private final Flux<Notificacion<Alumno>> alumnosNotificationFlux;
    // Para las notificaciones
    private FluxSink<Notificacion<Alumno>> alumnosNotification;

    private AlumnosNotificationImpl() {
        this.alumnosNotificationFlux = Flux.<Notificacion<Alumno>>create(emitter -> this.alumnosNotification = emitter).share();
    }

    public static AlumnosNotificationImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlumnosNotificationImpl();
        }
        return INSTANCE;
    }

    @Override
    public Flux<Notificacion<Alumno>> getNotificationAsFlux() {
        return alumnosNotificationFlux;
    }

    @Override
    public void notify(Notificacion<Alumno> notificacion) {
        alumnosNotification.next(notificacion);
    }
}
