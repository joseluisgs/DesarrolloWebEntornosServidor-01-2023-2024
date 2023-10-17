package dev.joseluisgs;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepositoryImpl;
import dev.joseluisgs.services.alumnado.AlumnosService;
import dev.joseluisgs.services.alumnado.AlumnosServiceImpl;
import dev.joseluisgs.services.database.DatabaseManager;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException {
        System.out.println("Hola Base de datos con Java y JDBC");

        // Ahora te lo voy a repetir con un repositorio, para que veas que todo queda encapsulado y más limpio
        AlumnosService alumnosService = AlumnosServiceImpl.getInstance(AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance()));

        // Obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosService.findAll().forEach(System.out::println);

        // Obtenemos un alumno por id
        System.out.println("Alumno con id 1");
        System.out.println(alumnosService.findById(1L));

        // Obtenemos los alumnos por nombre
        System.out.println("Alumnos con nombre Juan");
        alumnosService.findAllByNombre("Juan").forEach(System.out::println);

        // Insertamos 3 alumnos
        System.out.println("Insertamos 3 alumnos");
        var alumno = Alumno.builder().id(0L).nombre("Carolina").calificacion(7.0).build();
        System.out.println(alumnosService.save(alumno));
        alumno = Alumno.builder().id(0L).nombre("Sara").calificacion(6.75).build();
        System.out.println(alumnosService.save(alumno));
        alumno = Alumno.builder().id(0L).nombre("Enrique").calificacion(8.0).build();
        System.out.println(alumnosService.save(alumno));

        // Obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosService.findAll().forEach(System.out::println);

        // Actualizamos el alumno con id 1
        System.out.println("Actualizamos el alumno con id 1");
        // Lo buscamos
        alumno = alumnosService.findById(1L).orElseThrow();
        // Lo modificamos
        alumno.setNombre("Alumno Updated");
        alumno.setCalificacion(9.0);
        System.out.println(alumnosService.update(alumno));

        // obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosService.findAll().forEach(System.out::println);

        // Borramos el alumno con id 1
        System.out.println("Borramos el alumno con id 1");
        alumno = alumnosService.findById(1L).orElseThrow();
        var deleted = alumnosService.deleteById(alumno.getId());
        if (deleted) {
            System.out.println("Alumno borrado: " + alumno);
        } else {
            System.out.println("Alumno no borrado porque no existe");
        }

        // obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosService.findAll().forEach(System.out::println);

        // Actualizamos algo que no existe
        System.out.println("Actualizamos el alumno con id -99");
        // Lo buscamos
        try {
            alumno = Alumno.builder().id(-99L).nombre("Alumno Updated").calificacion(9.0).build();
            System.out.println(alumnosService.update(alumno));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
}