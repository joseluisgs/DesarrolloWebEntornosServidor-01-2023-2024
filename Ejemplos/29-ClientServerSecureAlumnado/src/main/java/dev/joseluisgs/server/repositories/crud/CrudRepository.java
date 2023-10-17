package dev.joseluisgs.server.repositories.crud;


import dev.joseluisgs.common.models.Alumno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Definimos la interfaz de nuestro repositorio, con los métodos que vamos a usar
// T es el tipo de dato que vamos a usar, y ID el tipo de dato de la clave primaria
public interface CrudRepository<T, ID> {
    // Métodos que vamos a usar
    // Buscar todos
    Flux<Alumno> findAll();

    // Buscar por ID
    Mono<Alumno> findById(ID id);

    // Guardar
    Mono<Alumno> save(T t);

    // Actualizar
    Mono<Alumno> update(T t);

    // Borrar por ID
    Mono<Boolean> deleteById(ID id);

    // Borrar todos
    Mono<Void> deleteAll();
}
