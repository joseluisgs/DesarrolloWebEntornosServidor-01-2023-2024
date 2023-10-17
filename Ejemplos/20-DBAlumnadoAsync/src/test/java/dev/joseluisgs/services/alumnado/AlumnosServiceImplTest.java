package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnosServiceImplTest {

    @Mock
    AlumnosRepository repository;

    @InjectMocks
    AlumnosServiceImpl service;

    @Test
    void findAll() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumnos = List.of(
                Alumno.builder().nombre("Test-1").calificacion(10.0).build(),
                Alumno.builder().nombre("Test-2").calificacion(9.0).build()
        );

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findAll()).thenReturn(CompletableFuture.completedFuture(alumnos));

        // Act
        var result = service.findAll();


        // Assert
        assertAll("findAll",
                () -> assertEquals(result.size(), 2, "No se han recuperado dos alumnos"),
                () -> assertEquals(result.get(0).getNombre(), "Test-1", "El primer alumno no es el esperado"),
                () -> assertEquals(result.get(1).getNombre(), "Test-2", "El segundo alumno no es el esperado"),
                () -> assertEquals(result.get(0).getCalificacion(), 10.0, "La calificación del primer alumno no es la esperada"),
                () -> assertEquals(result.get(1).getCalificacion(), 9.0, "La calificación del segundo alumno no es la esperada")
        );

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAllByNombre() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumnos = List.of(Alumno.builder().nombre("Test-1").calificacion(10.0).build());

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findByNombre("Test-1")).thenReturn(CompletableFuture.completedFuture(alumnos));

        // Act
        var result = service.findAllByNombre("Test-1");


        // Assert
        assertAll("findAllByNombre",
                () -> assertEquals(result.size(), 1, "No se ha recuperado un alumno"),
                () -> assertEquals(result.get(0).getNombre(), "Test-1", "El alumno no es el esperado"),
                () -> assertEquals(result.get(0).getCalificacion(), 10.0, "La calificación del alumno no es la esperada")
        );

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findByNombre("Test-1");
    }

    @Test
    void findById() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findById(1L)).thenReturn(CompletableFuture.completedFuture(Optional.of(alumno)));

        // Act
        var result = service.findById(1L);


        // Assert
        assertAll("findById",
                () -> assertEquals(result.get().getNombre(), "Test-1", "El alumno no es el esperado"),
                () -> assertEquals(result.get().getCalificacion(), 10.0, "La calificación del alumno no es la esperada")
        );

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findByIdNoExiste() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findById(1L)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // Act
        var result = service.findById(1L);
    }

    @Test
    void save() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.save(alumno)).thenReturn(CompletableFuture.completedFuture(alumno));

        // Act
        var result = service.save(alumno);


        // Assert
        assertAll("save",
                () -> assertEquals(result.getNombre(), "Test-1", "El alumno no es el esperado"),
                () -> assertEquals(result.getCalificacion(), 10.0, "La calificación del alumno no es la esperada")
        );

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).save(alumno);
    }

    @Test
    void update() throws SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.update(alumno)).thenReturn(CompletableFuture.completedFuture(alumno));

        // Act
        var result = service.update(alumno);


        // Assert
        assertAll("update",
                () -> assertEquals(result.getNombre(), "Test-1", "El alumno no es el esperado"),
                () -> assertEquals(result.getCalificacion(), 10.0, "La calificación del alumno no es la esperada")
        );

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).update(alumno);
    }

    @Test
    void updateNoExiste() throws SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().id(99L).nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos la excepción AlumnoNoEncotradoException...
        when(repository.update(alumno)).thenThrow(new AlumnoNoEncotradoException("Alumno no encontrado en la base de datos con id: 99L"));

        // Act
        try {
            var result = service.update(alumno);
        } catch (AlumnoNoEncotradoException ex) {
            // Assert
            assertEquals(ex.getMessage(), "Alumno no encontrado en la base de datos con id: 99L", "El mensaje de la excepción no es el esperado");
        }

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).update(alumno);
    }

    @Test
    void deleteById() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.deleteById(1L)).thenReturn(CompletableFuture.completedFuture(true));

        // Act
        var result = service.deleteById(1L);


        // Assert
        assertTrue(result, "No se ha borrado el alumno");

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdNoExiste() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.deleteById(1L)).thenReturn(CompletableFuture.completedFuture(false));

        // Act
        var result = service.deleteById(1L);
    }

    @Test
    void deleteAll() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.deleteAll()).thenReturn(CompletableFuture.completedFuture(null));

        // Act
        service.deleteAll();

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).deleteAll();
    }
}
