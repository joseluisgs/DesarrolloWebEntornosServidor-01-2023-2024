package dev.joseluisgs.repositories.crud;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

// Definimos la interfaz de nuestro repositorio, con los métodos que vamos a usar
// T es el tipo de dato que vamos a usar, y ID el tipo de dato de la clave primaria
public interface CrudRepository<T, ID> {
    // Métodos que vamos a usar
    // Guardar
    CompletableFuture<Alumno> save(T t) throws SQLException;

    // Actualizar
    CompletableFuture<Alumno> update(T t) throws SQLException, AlumnoNoEncotradoException;

    // Buscar por ID
    CompletableFuture<Optional<Alumno>> findById(ID id) throws SQLException;

    // Buscar todos
    CompletableFuture<List<Alumno>> findAll() throws SQLException;

    // Borrar por ID
    CompletableFuture<Boolean> deleteById(ID id) throws SQLException;

    // Borrar todos
    CompletableFuture<Void> deleteAll() throws SQLException;
}
