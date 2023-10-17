package dev.joseluisgs.repositories.alumnado;


import dev.joseluisgs.exceptions.alumnos.AlumnoException;
import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    void saveAlumno() throws SQLException, AlumnoException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno);
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
    void findAlumnoById() throws SQLException, AlumnoException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno);
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId());
        assertAll(() -> assertTrue(foundAlumno.isPresent()),
                () -> assertEquals(alumno.getNombre(), foundAlumno.get().getNombre()),
                () -> assertEquals(alumno.getCalificacion(), foundAlumno.get().getCalificacion()),
                () -> assertNotNull(foundAlumno.get().getUuid()),
                () -> assertNotNull(foundAlumno.get().getCreatedAt()),
                () -> assertNotNull(foundAlumno.get().getUpdatedAt())
        );
    }

    @Test
    void findAlumnoByIdNoExiste() throws SQLException {
        Optional<Alumno> foundAlumno = alumnosRepository.findById(1L);
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }

    @Test
    void findAllAlumnos() throws SQLException, AlumnoException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1);
        alumnosRepository.save(alumno2);
        List<Alumno> foundAlumnos = alumnosRepository.findAll();
        assertEquals(2, foundAlumnos.size());
    }

    @Test
    void findAlumnosByNombre() throws SQLException, AlumnoException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1);
        alumnosRepository.save(alumno2);
        List<Alumno> foundAlumnos = alumnosRepository.findByNombre("Test");
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
    void updateAlumno() throws SQLException, AlumnoException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno);
        savedAlumno.setNombre("Updated");
        savedAlumno.setCalificacion(8.5);
        alumnosRepository.update(savedAlumno);
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId());
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
        Exception exception = assertThrows(AlumnoNoEncotradoException.class, () -> {
            alumnosRepository.update(alumno);
        });
        String expectedMessage = "Alumno/a no encontrado con id: " + alumno.getId();

        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    void deleteAlumno() throws SQLException, AlumnoException {
        Alumno alumno = Alumno.builder()
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno);
        alumnosRepository.deleteById(savedAlumno.getId());
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId());
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }

    @Test
    void deleteAlumnoNoExiste() throws SQLException {
        boolean deleted = alumnosRepository.deleteById(99L);
        assertFalse(deleted);
    }

    @Test
    void deleteAllAlumnos() throws SQLException, AlumnoException {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1);
        alumnosRepository.save(alumno2);
        alumnosRepository.deleteAll();
        List<Alumno> foundAlumnos = alumnosRepository.findAll();
        assertEquals(0, foundAlumnos.size());
    }

}