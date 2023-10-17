package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.models.Notificacion;
import dev.joseluisgs.repositories.alumnado.AlumnosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class AlumnosServiceImpl implements AlumnosService {
    // Para mi cache
    private static final int CACHE_SIZE = 10; // Tamaño de la cache
    // Singleton
    private static AlumnosServiceImpl instance;
    private final AlumnosCache cache;
    private final AlumnosNotification notification;
    private final Logger logger = LoggerFactory.getLogger(AlumnosServiceImpl.class);
    private final AlumnosRepository alumnosRepository;

    private AlumnosServiceImpl(AlumnosRepository alumnosRepository, AlumnosNotification notification) {
        this.alumnosRepository = alumnosRepository;
        // Inicializamos la cache con el tamaño y la política de borrado de la misma
        // borramos el más antiguo cuando llegamos al tamaño máximo
        this.cache = new AlumnosCacheImpl(CACHE_SIZE);
        // Inicializamos la notificación
        this.notification = notification;

    }


    public static AlumnosServiceImpl getInstance(AlumnosRepository alumnosRepository, AlumnosNotification notification) {
        if (instance == null) {
            instance = new AlumnosServiceImpl(alumnosRepository, notification);
        }
        return instance;
    }

    @Override
    public Flux<Alumno> findAll() {
        logger.debug("Buscando todos los alumnos");
        return alumnosRepository.findAll();
    }

    @Override
    public Flux<Alumno> findAllByNombre(String nombre) {
        logger.debug("Buscando todos los alumnos por nombre");
        return alumnosRepository.findByNombre(nombre);
    }

    @Override
    public Mono<Alumno> findById(long id) {
        // Ojo con la excepcion si no existe que debemos lanzarla
        logger.debug("Buscando alumno por id: " + id);
        // Lo buscamos en la cache
        return cache.get(id)
                // Si no está en cache lo buscamos en la base de datos
                .switchIfEmpty(alumnosRepository.findById(id)
                        // Si lo encontramos lo añadimos a la cache
                        .flatMap(alumno -> cache.put(alumno.getId(), alumno)
                                // Y lo devolvemos
                                .then(Mono.just(alumno)))
                        // Si no lo encontramos, lanzamos una excepción
                        .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con id " + id + " no encontrado"))));
    }


    @Override
    public Mono<Alumno> findByUuid(UUID uuid) {
        logger.debug("Buscando alumno por uuid: " + uuid);
        // Lo buscamos en la base de datos, si existe a la cache, si no existe lanzamos excepción
        return alumnosRepository.findByUuid(uuid)
                // Si lo encontramos lo añadimos a la cache
                .flatMap(alumno -> cache.put(alumno.getId(), alumno)
                        // Y lo devolvemos
                        .then(Mono.just(alumno)))
                // Si no lo encontramos, lanzamos una excepción
                .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con uuid " + uuid + " no encontrado")));
    }

    private Mono<Alumno> saveWithoutNotification(Alumno alumno) {
        // Hacemos esto para testar solo este método y no el save con notificaciones por los problemas que da el doOnSuccess
        // y porque nos falta "base" para testearlo
        logger.debug("Guardando alumno sin notificación: " + alumno);
        return alumnosRepository.save(alumno)
                .flatMap(saved -> findByUuid(saved.getUuid()));
    }

    @Override
    public Mono<Alumno> save(Alumno alumno) {
        logger.debug("Guardando alumno: " + alumno);
        return saveWithoutNotification(alumno)
                .doOnSuccess(saved -> notification.notify(new Notificacion<>(Notificacion.Tipo.NEW, saved)));
    }


    private Mono<Alumno> updateWithoutNotification(Alumno alumno) {
        // Hacemos esto para testar solo este método y no el update con notificaciones por los problemas que da el doOnSuccess
        // y porque nos falta "base" para testearlo
        logger.debug("Actualizando alumno sin notificación: " + alumno);
        return alumnosRepository.findById(alumno.getId())
                .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con id " + alumno.getId() + " no encontrado")))
                .flatMap(existing -> alumnosRepository.update(alumno)
                        .flatMap(updated -> cache.put(updated.getId(), updated)
                                .thenReturn(updated)));
    }


    @Override
    public Mono<Alumno> update(Alumno alumno) {
        logger.debug("Actualizando alumno: " + alumno);
        return updateWithoutNotification(alumno)
                .doOnSuccess(updated -> notification.notify(new Notificacion<>(Notificacion.Tipo.UPDATED, updated)));
    }

    private Mono<Alumno> deleteByIdWithoutNotification(long id) {
        // Hacemos esto para testar solo este método y no el delete con notificaciones por los problemas que da el doOnSuccess
        // y porque nos falta "base" para testearlo
        logger.debug("Borrando alumno sin notificación con id: " + id);
        return alumnosRepository.findById(id)
                .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con id " + id + " no encontrado")))
                .flatMap(alumno -> cache.remove(alumno.getId())
                        .then(alumnosRepository.deleteById(alumno.getId()))
                        .thenReturn(alumno));
    }

    @Override
    public Mono<Alumno> deleteById(long id) {
        logger.debug("Borrando alumno por id: " + id);
        return deleteByIdWithoutNotification(id)
                .doOnSuccess(deleted -> notification.notify(new Notificacion<>(Notificacion.Tipo.DELETED, deleted)));
    }


    @Override
    public Mono<Void> deleteAll() {
        // limpiamos la cache y la base de datos
        logger.debug("Borrando todos los alumnos");
        cache.clear();
        return alumnosRepository.deleteAll()
                .then(Mono.empty());
    }

    public Flux<Notificacion<Alumno>> getNotifications() {
        return notification.getNotificationAsFlux();
    }
}
