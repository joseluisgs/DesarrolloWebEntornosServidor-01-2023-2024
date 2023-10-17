package dev.joseluisgs.repositories.alumnado;


import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cuidado con las fechas y UUId si se generan la base de datos o las cambiamos
 */

class alumnosRepositoryTestDB {

    private AlumnosRepository alumnosRepository;

    @BeforeEach
    void setUp() throws SQLException {
        alumnosRepository = AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance());
        alumnosRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        alumnosRepository.deleteAll();
    }

    @Test
    void saveAlumno() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).get();
        assertAll(() -> assertNotNull(savedAlumno),
                () -> assertNotNull(savedAlumno.getId()),
                () -> assertEquals(alumno.getNombre(), savedAlumno.getNombre()),
                () -> assertEquals(alumno.getCalificacion(), savedAlumno.getCalificacion()),
                () -> assertNotNull(savedAlumno.getUuid()),
                () -> assertNotNull(savedAlumno.getCreatedAt()),
                () -> assertNotNull(savedAlumno.getUpdatedAt())
        );
    }

    @Test
    void findAlumnoById() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).get();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).get();
        assertAll(() -> assertTrue(foundAlumno.isPresent()),
                () -> assertEquals(alumno.getNombre(), foundAlumno.get().getNombre()),
                () -> assertEquals(alumno.getCalificacion(), foundAlumno.get().getCalificacion()),
                () -> assertNotNull(foundAlumno.get().getUuid()),
                () -> assertNotNull(foundAlumno.get().getCreatedAt()),
                () -> assertNotNull(foundAlumno.get().getUpdatedAt())
        );
    }

    @Test
    void findAlumnoByIdNoExiste() throws SQLException, ExecutionException, InterruptedException {
        Optional<Alumno> foundAlumno = alumnosRepository.findById(1L).get();
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }

    @Test
    void findAllAlumnos() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).get();
        alumnosRepository.save(alumno2).get();
        List<Alumno> foundAlumnos = alumnosRepository.findAll().get();
        assertEquals(2, foundAlumnos.size());
    }

    @Test
    void findAlumnosByNombre() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).get();
        alumnosRepository.save(alumno2).get();
        List<Alumno> foundAlumnos = alumnosRepository.findByNombre("Test").get();
        System.out.println(foundAlumnos);
        assertAll(() -> assertNotNull(foundAlumnos),
                () -> assertEquals(2, foundAlumnos.size()),
                () -> assertEquals(foundAlumnos.get(0).getNombre(), alumno1.getNombre()),
                () -> assertEquals(foundAlumnos.get(0).getCalificacion(), alumno1.getCalificacion()),
                () -> assertEquals(foundAlumnos.get(1).getNombre(), alumno2.getNombre()),
                () -> assertEquals(foundAlumnos.get(1).getCalificacion(), alumno2.getCalificacion())
        );
    }

    @Test
    void updateAlumno() throws SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).get();
        savedAlumno.setNombre("Updated");
        savedAlumno.setCalificacion(8.5);
        alumnosRepository.update(savedAlumno).get();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).get();
        assertAll(() -> assertTrue(foundAlumno.isPresent()),
                () -> assertEquals(savedAlumno.getNombre(), foundAlumno.get().getNombre()),
                () -> assertEquals(savedAlumno.getCalificacion(), foundAlumno.get().getCalificacion())
        );
    }

    @Test
    void updateAlumnoNoExiste() throws SQLException {
        Alumno alumno = Alumno.builder()
                .id(99L)
                .nombre("Test")
                .calificacion(9.5)
                .build();

        // Comprobamos que se lanza la expectation AlumnoNoEncotradoExcepction con el mensaje esperado
        Exception exception = assertThrows(ExecutionException.class, () -> {
            alumnosRepository.update(alumno).get();
        });
        String expectedMessage = "Alumno/a no encontrado con id: " + alumno.getId();

        assertTrue(exception.getMessage().contains(expectedMessage));

    }

    @Test
    void deleteAlumno() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).get();
        alumnosRepository.deleteById(savedAlumno.getId()).get();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).get();
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }

    @Test
    void deleteAlumnoNoExiste() throws SQLException, ExecutionException, InterruptedException {
        boolean deleted = alumnosRepository.deleteById(99L).get();
        assertFalse(deleted);
    }

    @Test
    void deleteAllAlumnos() throws SQLException, ExecutionException, InterruptedException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).get();
        alumnosRepository.save(alumno2).get();
        alumnosRepository.deleteAll().get();
        List<Alumno> foundAlumnos = alumnosRepository.findAll().get();
        assertEquals(0, foundAlumnos.size());
    }

}