package dev.joseluisgs.repositories.alumnado;


import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cuidado con las fechas y UUId si se generan la base de datos o las cambiamos
 */

class AlumnosRepositoryImplTest {

    private AlumnosRepository alumnosRepository;

    @BeforeEach
    void setUp() throws SQLException {
        alumnosRepository = AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance());
        DatabaseManager.getInstance().initTables();

    }


    @Test
    void saveAlumno() {
        Alumno alumno = Alumno.builder()
                .id(1L) // Cuidado con los IDS que se general automaticamente, mejor los uuid para todo eh! :)
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).block();
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
    void findAlumnoById() {
        Alumno alumno = Alumno.builder()
                .id(1L) // Cuidado con los IDS que se general automaticamente, mejor los uuid para todo eh! :)
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).block();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).blockOptional();
        assertAll(
                () -> assertEquals(alumno.getNombre(), foundAlumno.get().getNombre()),
                () -> assertEquals(alumno.getCalificacion(), foundAlumno.get().getCalificacion()),
                () -> assertNotNull(foundAlumno.get().getUuid()),
                () -> assertNotNull(foundAlumno.get().getCreatedAt()),
                () -> assertNotNull(foundAlumno.get().getUpdatedAt())
        );
    }

    @Test
    void findAlumnoByIdNoExiste() {
        Optional<Alumno> foundAlumno = alumnosRepository.findById(1L).blockOptional();
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }

    @Test
    void findAllAlumnos() {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).block();
        alumnosRepository.save(alumno2).block();
        List<Alumno> foundAlumnos = alumnosRepository.findAll().collectList().block();
        assertEquals(2, foundAlumnos.size());
    }

    @Test
    void findAlumnosByNombre() {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).block();
        alumnosRepository.save(alumno2).block();
        List<Alumno> foundAlumnos = alumnosRepository.findByNombre("Test").collectList().block();
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
    void updateAlumno() {
        Alumno alumno = Alumno.builder()
                .id(1L)
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).block();
        savedAlumno.setNombre("Updated");
        savedAlumno.setCalificacion(8.5);
        alumnosRepository.update(savedAlumno).block();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).blockOptional();
        assertAll(() -> assertTrue(foundAlumno.isPresent()),
                () -> assertEquals(savedAlumno.getNombre(), foundAlumno.get().getNombre()),
                () -> assertEquals(savedAlumno.getCalificacion(), foundAlumno.get().getCalificacion())
        );
    }

    @Test
    void deleteAlumno() {
        Alumno alumno = Alumno.builder()
                .id(1L)
                .nombre("Test")
                .calificacion(9.5)
                .build();
        Alumno savedAlumno = alumnosRepository.save(alumno).block();
        alumnosRepository.deleteById(savedAlumno.getId()).block();
        Optional<Alumno> foundAlumno = alumnosRepository.findById(savedAlumno.getId()).blockOptional();
        assertAll(() -> assertFalse(foundAlumno.isPresent())
        );
    }


    @Test
    void deleteAllAlumnos() {
        Alumno alumno1 = Alumno.builder()
                .nombre("Test-1")
                .calificacion(9.5)
                .build();
        Alumno alumno2 = Alumno.builder()
                .nombre("Test-2")
                .calificacion(8.5)
                .build();
        alumnosRepository.save(alumno1).block();
        alumnosRepository.save(alumno2).block();
        alumnosRepository.deleteAll().block();
        List<Alumno> foundAlumnos = alumnosRepository.findAll().collectList().block();
        assertEquals(0, foundAlumnos.size());
    }

    // OJO, los id no existen ya no los comparamos aquí porque han salido al servicio

}