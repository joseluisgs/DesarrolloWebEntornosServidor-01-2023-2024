package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoException;
import dev.joseluisgs.models.Alumno;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// CRUD de Alumnos
public interface AlumnosService {
    List<Alumno> findAll() throws SQLException;

    List<Alumno> findAllByNombre(String nombre) throws SQLException;

    Optional<Alumno> findById(long id) throws SQLException;

    Alumno save(Alumno alumno) throws SQLException, AlumnoException;

    Alumno update(Alumno alumno) throws SQLException, AlumnoException;

    boolean deleteById(long id) throws SQLException;

    void deleteAll() throws SQLException;
}
