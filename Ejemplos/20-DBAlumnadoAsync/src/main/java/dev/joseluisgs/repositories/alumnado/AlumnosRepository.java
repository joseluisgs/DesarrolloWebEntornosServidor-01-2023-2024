package dev.joseluisgs.repositories.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Cogemos la interfaz Crud la contextualizamos a nuestro tipo y añadimos metodos sin falta
// Por ejeplo un FibByNombre
public interface AlumnosRepository extends CrudRepository<Alumno, Long> {
    // Buscar por nombre
    CompletableFuture<List<Alumno>> findByNombre(String nombre) throws SQLException;
}
