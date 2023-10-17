package dev.joseluisgs.repositories.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public class AlumnosRepositoryImpl implements AlumnosRepository {

    private static AlumnosRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(AlumnosRepositoryImpl.class);
    // private final ConnectionFactory connectionFactory;
    private final ConnectionPool connectionFactory;

    /*private AlumnosRepositoryImpl(DatabaseManager databaseManager) {
        this.connectionFactory = databaseManager.getConnectionFactory();
    }*/

    private AlumnosRepositoryImpl(DatabaseManager databaseManager) {
        this.connectionFactory = databaseManager.getConnectionPool();
    }

    public static AlumnosRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new AlumnosRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public Flux<Alumno> findAll() {
        logger.debug("Buscando todos los alumnos");
        String sql = "SELECT * FROM ALUMNOS";
        return Flux.usingWhen(
                connectionFactory.create(),
                connection -> Flux.from(connection.createStatement(sql).execute())
                        .flatMap(result -> result.map((row, rowMetadata) ->
                                Alumno.builder()
                                        .id(row.get("id", Long.class))
                                        .nombre(row.get("nombre", String.class))
                                        .calificacion(row.get("calificacion", Float.class).doubleValue())
                                        .uuid(row.get("uuid", java.util.UUID.class))
                                        .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                        .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                        .build()
                        )),
                Connection::close
        );
    }

    @Override
    public Flux<Alumno> findByNombre(String nombre) {
        logger.debug("Buscando todos los alumnos por nombre");
        String sql = "SELECT * FROM ALUMNOS WHERE nombre LIKE ?";
        return Flux.usingWhen(
                connectionFactory.create(),
                connection -> Flux.from(connection.createStatement(sql)
                        .bind(0, "%" + nombre + "%")
                        .execute()
                ).flatMap(result -> result.map((row, rowMetadata) ->
                        Alumno.builder()
                                .id(row.get("id", Long.class))
                                .nombre(row.get("nombre", String.class))
                                .calificacion(row.get("calificacion", Float.class).doubleValue())
                                .uuid(row.get("uuid", java.util.UUID.class))
                                .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                .build()
                )),
                Connection::close
        );
    }

    @Override
    public Mono<Alumno> findById(Long id) {
        logger.debug("Buscando alumno por id: " + id);
        String sql = "SELECT * FROM ALUMNOS WHERE id = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, id)
                        .execute()
                ).flatMap(result -> Mono.from(result.map((row, rowMetadata) ->
                        Alumno.builder()
                                .id(row.get("id", Long.class))
                                .nombre(row.get("nombre", String.class))
                                .calificacion(row.get("calificacion", Float.class).doubleValue())
                                .uuid(row.get("uuid", java.util.UUID.class))
                                .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                .build()
                ))),
                Connection::close
        );
    }

    @Override
    public Mono<Alumno> findByUuid(UUID uuid) {
        logger.debug("Buscando alumno por uuid: " + uuid);
        String sql = "SELECT * FROM ALUMNOS WHERE uuid = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, uuid)
                        .execute()
                ).flatMap(result -> Mono.from(result.map((row, rowMetadata) ->
                        Alumno.builder()
                                .id(row.get("id", Long.class))
                                .nombre(row.get("nombre", String.class))
                                .calificacion(row.get("calificacion", Float.class).doubleValue())
                                .uuid(row.get("uuid", java.util.UUID.class))
                                .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                .build()
                ))),
                Connection::close
        );
    }

    @Override
    public Mono<Alumno> save(Alumno alumno) {
        logger.debug("Guardando alumno: " + alumno);
        String sql = "INSERT INTO ALUMNOS (nombre, calificacion, uuid, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, alumno.getNombre())
                        .bind(1, alumno.getCalificacion())
                        .bind(2, alumno.getUuid())
                        .bind(3, alumno.getCreatedAt())
                        .bind(4, alumno.getUpdatedAt())
                        .execute()
                ).then(Mono.just(alumno)), // Aquí devolvemos el objeto 'alumno' después de la inserción
                Connection::close
        );
    }

    @Override
    public Mono<Alumno> update(Alumno alumno) {
        logger.debug("Actualizando alumno: " + alumno);
        String query = "UPDATE ALUMNOS SET nombre = ?, calificacion = ?, updated_at = ? WHERE id = ?";
        alumno.setUpdatedAt(LocalDateTime.now());
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(query)
                        .bind(0, alumno.getNombre())
                        .bind(1, alumno.getCalificacion())
                        .bind(2, alumno.getUpdatedAt())
                        .bind(3, alumno.getId())
                        .execute()
                ).then(Mono.just(alumno)), // Aquí devolvemos el objeto 'alumno' después de la actualización
                Connection::close
        );
    }


    @Override
    public Mono<Boolean> deleteById(Long id) {
        logger.debug("Borrando alumno por id: " + id);
        String sql = "DELETE FROM ALUMNOS WHERE id = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                                .bind(0, id)
                                .execute()
                        ).flatMapMany(Result::getRowsUpdated)
                        .hasElements(),
                Connection::close
        );
    }

    @Override
    public Mono<Void> deleteAll() {
        logger.debug("Borrando todos los alumnos");
        String sql = "DELETE FROM ALUMNOS";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .execute()
                ).then(),
                Connection::close
        );
    }
}
