package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

// CRUD de Alumnos
public interface AlumnosService {
    Flux<Alumno> findAll();

    Flux<Alumno> findAllByNombre(String nombre);

    Mono<Alumno> findById(long id);

    Mono<Alumno> findByUuid(UUID uuid);

    Mono<Alumno> save(Alumno alumno);

    Mono<Alumno> update(Alumno alumno);

    Mono<Alumno> deleteById(long id);

    Mono<Void> deleteAll();
}
