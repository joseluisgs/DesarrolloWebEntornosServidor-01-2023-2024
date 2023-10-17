package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnosServiceImplTest {

    @Mock
    AlumnosRepository repository;

    @Mock
    AlumnosNotification notification;

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
        when(repository.findAll()).thenReturn(Flux.fromIterable(alumnos));

        // Act
        var result = service.findAll().collectList().block();


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
        when(repository.findByNombre("Test-1")).thenReturn(Flux.fromIterable(alumnos));

        // Act
        var result = service.findAllByNombre("Test-1").collectList().block();


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
        when(repository.findById(1L)).thenReturn(Mono.just(alumno));

        // Act
        var result = service.findById(1L).blockOptional();


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
        when(repository.findById(1L)).thenReturn(Mono.empty());

        var res = assertThrows(Exception.class, () -> service.findById(1L).blockOptional());
        assertTrue(res.getMessage().contains("Alumno con id 1 no encontrado"));

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void save() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().id(1L).nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findByUuid(alumno.getUuid())).thenReturn(Mono.just(alumno));
        when(repository.save(alumno)).thenReturn(Mono.just(alumno));

        // Act
        var result = service.save(alumno).block(); // Error por la notificacion
        //var result = service.saveWithoutNotification(alumno).block();


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
        var alumno = Alumno.builder().id(1L).nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.findById(1L)).thenReturn(Mono.just(alumno));
        when(repository.update(alumno)).thenReturn(Mono.just(alumno));

        // Act
        // No lo vamos a poer testear bien por la notificación, no te preocupes
        // var result = service.updateWithoutNotification(alumno).block();
        var result = service.update(alumno).block();


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
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();
        when(repository.findById(1L)).thenReturn(Mono.empty());

        // Act
        var res = assertThrows(Exception.class, () -> service.deleteById(1L).blockOptional());
        assertTrue(res.getMessage().contains("Alumno con id 1 no encontrado"));

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deleteById() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().id(1L).nombre("Test-1").calificacion(10.0).build();
        when(repository.findById(1L)).thenReturn(Mono.just(alumno));
        when(repository.deleteById(1L)).thenReturn(Mono.just(true));

        // Act
        // var result = service.deleteByIdWithoutNotification(1L).block();
        var result = service.deleteById(1L).block();

        // Assert
        assertEquals(result, alumno, "El alumno no es el esperado");

    }

    @Test
    void deleteByIdNoExiste() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();
        when(repository.findById(1L)).thenReturn(Mono.empty());

        // Act
        var res = assertThrows(Exception.class, () -> service.deleteById(1L).blockOptional());
        assertTrue(res.getMessage().contains("Alumno con id 1 no encontrado"));

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).findById(1L);

    }

    @Test
    void deleteAll() throws SQLException, ExecutionException, InterruptedException {
        // Arrange
        var alumno = Alumno.builder().nombre("Test-1").calificacion(10.0).build();

        // Cuando se llame al método al repositorio simulamos...
        when(repository.deleteAll()).thenReturn(Mono.empty());

        // Act
        service.deleteAll().block();

        // Comprobamos que se ha llamado al método del repositorio
        verify(repository, times(1)).deleteAll();
    }
}
