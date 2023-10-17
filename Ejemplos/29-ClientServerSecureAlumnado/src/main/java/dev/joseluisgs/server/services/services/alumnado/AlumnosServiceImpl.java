package dev.joseluisgs.server.services.services.alumnado;


import dev.joseluisgs.common.models.Alumno;
import dev.joseluisgs.server.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.server.repositories.alumnado.AlumnosRepository;
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
    private final Logger logger = LoggerFactory.getLogger(AlumnosServiceImpl.class);
    private final AlumnosRepository alumnosRepository;


    private AlumnosServiceImpl(AlumnosRepository alumnosRepository) {
        this.alumnosRepository = alumnosRepository;
        // Inicializamos la cache con el tamaño y la política de borrado de la misma
        // borramos el más antiguo cuando llegamos al tamaño máximo
        this.cache = new AlumnosCacheImpl(CACHE_SIZE);
        // Inicializamos la notificación

    }


    public static AlumnosServiceImpl getInstance(AlumnosRepository alumnosRepository) {
        if (instance == null) {
            instance = new AlumnosServiceImpl(alumnosRepository);
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

    @Override
    public Mono<Alumno> save(Alumno alumno) {
        logger.debug("Guardando alumno: " + alumno);
        // Salvamos, lo añadimos a la cache, y lo devolvemos con su uuid y avisamos a la notificación
        // Si lo hacemos así va a ser dificil de testear por el doOnNext
        return alumnosRepository.save(alumno)
                .flatMap(saved -> findByUuid(saved.getUuid()));
    }

    @Override
    public Mono<Alumno> update(Alumno alumno) {
        logger.debug("Actualizando alumno: " + alumno);
        // Si el alumno existe lo actualizamos, lo añadimos a la cache y lo devolvemos y avisamos a la notificación
        return alumnosRepository.findById(alumno.getId())
                .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con id " + alumno.getId() + " no encontrado")))
                .flatMap(existing -> alumnosRepository.update(alumno)
                        .flatMap(updated -> cache.put(updated.getId(), updated)
                                .thenReturn(updated)));
    }


    @Override
    public Mono<Alumno> deleteById(long id) {
        logger.debug("Borrando alumno por id: " + id);
        // Si el alumno existe lo borramos, lo borramos de la cache y lo devolvemos y avisamos a la notificación

        //Si lo hacemos así va a ser más dificil de testear por el doOnSuccess
        return alumnosRepository.findById(id)
                .switchIfEmpty(Mono.error(new AlumnoNoEncotradoException("Alumno con id " + id + " no encontrado")))
                .flatMap(alumno -> cache.remove(alumno.getId())
                        .then(alumnosRepository.deleteById(alumno.getId()))
                        .thenReturn(alumno));
    }


    @Override
    public Mono<Void> deleteAll() {
        // limpiamos la cache y la base de datos
        logger.debug("Borrando todos los alumnos");
        cache.clear();
        return alumnosRepository.deleteAll()
                .then(Mono.empty());
    }
}
