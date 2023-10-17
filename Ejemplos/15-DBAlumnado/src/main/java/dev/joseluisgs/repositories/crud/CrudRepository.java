package dev.joseluisgs.repositories.crud;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Definimos la interfaz de nuestro repositorio, con los métodos que vamos a usar
// T es el tipo de dato que vamos a usar, y ID el tipo de dato de la clave primaria
public interface CrudRepository<T, ID, EX extends Exception> {
    // Métodos que vamos a usar
    // Guardar
    T save(T t) throws SQLException, EX;

    // Actualizar
    T update(T t) throws SQLException, EX;

    // Buscar por ID
    Optional<T> findById(ID id) throws SQLException;

    // Buscar todos
    List<T> findAll() throws SQLException;

    // Borrar por ID
    boolean deleteById(ID id) throws SQLException;

    // Borrar todos
    void deleteAll() throws SQLException;
}
