package dev.joseluisgs;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepositoryImpl;
import dev.joseluisgs.services.alumnado.AlumnosNotificationImpl;
import dev.joseluisgs.services.alumnado.AlumnosServiceImpl;
import dev.joseluisgs.services.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola Base de datos con Java y R2DBC");
        // DatabaseManager databaseManager = DatabaseManager.getInstance();
        //AlumnosRepository alumnosRepository = AlumnosRepositoryImpl.getInstance(databaseManager);

        AlumnosServiceImpl alumnosService = AlumnosServiceImpl.getInstance(
                AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance()),
                AlumnosNotificationImpl.getInstance()
        );
        // AlumnosNotification alumnosNotification = AlumnosNotificationImpl.getInstance();

        System.out.println("Sistema de obtención de notificaciones en Tiempo Real");
        alumnosService.getNotifications().subscribe(
                notificacion -> {
                    switch (notificacion.getTipo()) {
                        case NEW:
                            System.out.println("🟢 Alumno insertado: " + notificacion.getContenido());
                            break;
                        case UPDATED:
                            System.out.println("🟠 Alumno actualizado: " + notificacion.getContenido());
                            break;
                        case DELETED:
                            System.out.println("🔴 Alumno eliminado: " + notificacion.getContenido());
                            break;
                    }
                },
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );

        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Si no queremos usar el suscribe podemos usar block y blockOptional
        System.out.println("Obtenemos todos los alumnos");
        System.out.println("Alumnos: " + alumnosService.findAll().collectList().block());
        alumnosService.findAll().collectList().blockOptional().ifPresentOrElse(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                () -> System.out.println("No hay alumnos")
        );

        // Insertamos 3 alumnos
        System.out.println("Insertamos 3 alumnos");
        var alumno = Alumno.builder().id(0L).nombre("Carolina").calificacion(7.0).build();
        alumnosService.save(alumno).subscribe(
                alumnoInsertado -> System.out.println("Alumno insertado: " + alumnoInsertado),
                error -> System.err.println("Error al insertar: " + error.getMessage()),
                () -> System.out.println("Inserción completada")
        );
        alumno = Alumno.builder().id(0L).nombre("Sara").calificacion(8.0).build();
        alumnosService.save(alumno).subscribe(
                alumnoInsertado -> System.out.println("Alumno insertado: " + alumnoInsertado),
                error -> System.err.println("Error al insertar: " + error.getMessage()),
                () -> System.out.println("Inserción completada")
        );
        alumno = Alumno.builder().id(0L).nombre("Pedro").calificacion(9.0).build();
        alumnosService.save(alumno).subscribe(
                alumnoInsertado -> System.out.println("Alumno insertado: " + alumnoInsertado),
                error -> System.err.println("Error al insertar: " + error.getMessage()),
                () -> System.out.println("Inserción completada")
        );

        // Obtenemos todos los alumnos
        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Alumno con id 1
        System.out.println("Obtenemos el alumno con id 1");
        alumnosService.findById(1L).subscribe(
                alumnoEncontrado -> System.out.println("Alumno encontrado: " + alumnoEncontrado),
                error -> System.err.println("Error al obtener el alumno: " + error.getMessage()),
                () -> System.out.println("Obtención de alumno completada")
        );


        // Alumno con uuid del alumno
        alumno = alumnosService.findById(1L).block();
        System.out.println("Obtenemos el alumno con uuid: " + alumno.getUuid());
        alumnosService.findByUuid(alumno.getUuid()).subscribe(
                alumnoEncontrado -> System.out.println("Alumno encontrado: " + alumnoEncontrado),
                error -> System.err.println("Error al obtener el alumno: " + error.getMessage()),
                () -> System.out.println("Obtención de alumno completada")
        );

        // Alumnos con nombre Carolina
        System.out.println("Obtenemos los alumnos con nombre Carolina");
        alumnosService.findAllByNombre("Carolina").collectList().subscribe(
                alumnos -> System.out.println("Alumnos de nombre Carolina: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Actualizamos el alumno con id 1
        System.out.println("Actualizamos el alumno con id 1");
        alumno = Alumno.builder().id(1L).nombre("CarolinaUpdated").calificacion(10.0).build();
        alumnosService.update(alumno).subscribe(
                alumnoActualizado -> System.out.println("Alumno actualizado: " + alumnoActualizado),
                error -> System.err.println("Error al actualizar: " + error.getMessage()),
                () -> System.out.println("Actualización completada")
        );

        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Borramos el alumno con id 1
        System.out.println("Borramos el alumno con id 1");
        alumnosService.deleteById(1L).subscribe(
                alumnoBorrado -> System.out.println("Alumno borrado: " + alumnoBorrado),
                error -> System.err.println("Error al borrar: " + error.getMessage()),
                () -> System.out.println("Borrado completado")
        );

        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Borramos el alumno con id 99
        System.out.println("Borramos el alumno con id 99");
        alumnosService.deleteById(99L).subscribe(
                alumnoBorrado -> System.out.println("Alumno borrado: " + alumnoBorrado),
                error -> System.err.println("Error al borrar: " + error.getMessage()),
                () -> System.out.println("Borrado completado")
        );

        // Tambien podemos usar blockOptional o block, pero con try catch
        try {
            System.out.println("Borramos el alumno con id 99");
            System.out.println("Alumno borrado: " + alumnosService.deleteById(99L).block());
        } catch (Exception e) {
            System.err.println("Error al borrar: " + e.getMessage());
        }
        /*System.out.println("Borramos el alumno con id 99");
        alumnosService.deleteById(99L).blockOptional().ifPresentOrElse(
                alumnoBorrado -> System.out.println("Alumno borrado: " + alumnoBorrado),
                () -> System.out.println("No se ha borrado el alumno")
        );*/

        // Obtenemos todos los alumnos
        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        // Borramos todos los alumnos
        System.out.println("Borramos todos los alumnos");
        alumnosService.deleteAll().subscribe(
                alumnoBorrado -> System.out.println("Alumno borrado: " + alumnoBorrado),
                error -> System.err.println("Error al borrar: " + error.getMessage()),
                () -> System.out.println("Borrado completado")
        );

        // Obtenemos todos los alumnos
        System.out.println("Obtenemos todos los alumnos");
        alumnosService.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de alumnos completada")
        );

        System.exit(0);
    }


}