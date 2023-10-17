package dev.joseluisgs.repositories.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;

// Cogemos la interfaz Crud la contextualizamos a nuestro tipo y añadimos metodos sin falta
// Por ejeplo un FibByNombre
public interface AlumnosRepository extends CrudRepository<Alumno, Long, AlumnoException> {
    // Buscar por nombre
    List<Alumno> findByNombre(String nombre) throws SQLException;
}
