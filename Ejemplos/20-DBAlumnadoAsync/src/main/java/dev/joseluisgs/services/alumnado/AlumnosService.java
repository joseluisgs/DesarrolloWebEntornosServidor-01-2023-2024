package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

// CRUD de Alumnos
public interface AlumnosService {
    List<Alumno> findAll() throws SQLException, ExecutionException, InterruptedException;

    List<Alumno> findAllByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException;

    Optional<Alumno> findById(long id) throws SQLException, ExecutionException, InterruptedException;

    Alumno save(Alumno alumno) throws SQLException, ExecutionException, InterruptedException;

    Alumno update(Alumno alumno) throws SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException;

    boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException;

    void deleteAll() throws SQLException, ExecutionException, InterruptedException;
}
