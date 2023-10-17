package dev.joseluisgs.repositories.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoAlmacenadoException;
import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

// Esta es la clase que se encarga de la persistencia de los alumnos
public class AlumnosRepositoryImpl implements AlumnosRepository {
    // Singleton
    private static AlumnosRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(AlumnosRepositoryImpl.class);
    // Base de datos
    private final DatabaseManager db;

    private AlumnosRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }


    public static AlumnosRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new AlumnosRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public CompletableFuture<List<Alumno>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<Alumno> lista = new ArrayList<>();
            String query = "SELECT * FROM ALUMNOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Obteniendo todos los alumnos");
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    Alumno alumno = Alumno.builder()
                            .id(rs.getLong("id"))
                            .nombre(rs.getString("nombre"))
                            .calificacion(rs.getDouble("calificacion"))
                            .uuid(rs.getObject("uuid", UUID.class))
                            .createdAt(rs.getObject("created_at", LocalDateTime.class))
                            .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                            .build();
                    lista.add(alumno);
                }
            } catch (SQLException e) {
                logger.error("Error al buscar todos los alumnos", e);
                throw new CompletionException(e);
            }
            return lista;
        });
    }


    @Override
    public CompletableFuture<List<Alumno>> findByNombre(String nombre) {
        return CompletableFuture.supplyAsync(() -> {
            var lista = new ArrayList<Alumno>();
            String query = "SELECT * FROM ALUMNOS WHERE nombre LIKE ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Obteniendo todos los alumnos por nombre que contenga: " + nombre);
                // Vamos a usar Like para buscar por nombre
                stmt.setString(1, "%" + nombre + "%");
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    // Creamos un alumno
                    Alumno alumno = Alumno.builder()
                            .id(rs.getLong("id"))
                            .nombre(rs.getString("nombre"))
                            .calificacion(rs.getDouble("calificacion"))
                            .uuid(rs.getObject("uuid", UUID.class))
                            .createdAt(rs.getObject("created_at", LocalDateTime.class))
                            .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                            .build();
                    // Lo añadimos a la lista
                    lista.add(alumno);
                }
            } catch (SQLException e) {
                logger.error("Error al buscar alumnos por nombre", e);
                throw new CompletionException(e);
            }
            return lista;
        });
    }

    @Override
    public CompletableFuture<Optional<Alumno>> findById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Alumno> alumno = Optional.empty();
            String query = "SELECT * FROM ALUMNOS WHERE id =?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                stmt.setLong(1, id);
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    alumno = Optional.of(Alumno.builder()
                            .id(rs.getLong("id"))
                            .nombre(rs.getString("nombre"))
                            .calificacion(rs.getDouble("calificacion"))
                            .uuid(rs.getObject("uuid", UUID.class))
                            .createdAt(rs.getObject("created_at", LocalDateTime.class))
                            .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                            .build()
                    );
                }
            } catch (SQLException e) {
                logger.error("Error al buscar alumno por id", e);
                throw new CompletionException(e);
            }
            return alumno;
        });
    }

    @Override
    public CompletableFuture<Alumno> save(Alumno alumno) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO ALUMNOS (nombre, calificacion, uuid, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ) {
                logger.debug("Guardando el alumno: " + alumno);
                stmt.setString(1, alumno.getNombre());
                stmt.setDouble(2, alumno.getCalificacion());
                stmt.setObject(3, alumno.getUuid());
                stmt.setObject(4, alumno.getCreatedAt());
                stmt.setObject(5, alumno.getUpdatedAt());
                var res = stmt.executeUpdate();
                if (res > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    while (rs.next()) {
                        alumno.setId(rs.getLong(1));
                    }
                    rs.close();
                } else {
                    logger.error("Alumno no guardado");
                    throw new AlumnoNoAlmacenadoException("Alumno/a no guardado con id: " + alumno.getId());
                }
            } catch (SQLException | AlumnoNoAlmacenadoException e) {
                logger.error("Error al guardar el alumno", e);
                throw new CompletionException(e);
            }
            return alumno;
        });
    }

    @Override
    public CompletableFuture<Alumno> update(Alumno alumno) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "UPDATE ALUMNOS SET nombre = ?, calificacion = ?, updated_at = ? WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Actualizando el alumno: " + alumno);
                alumno.setUpdatedAt(LocalDateTime.now());
                stmt.setString(1, alumno.getNombre());
                stmt.setDouble(2, alumno.getCalificacion());
                stmt.setObject(3, alumno.getUpdatedAt());
                stmt.setLong(4, alumno.getId());
                var res = stmt.executeUpdate();
                if (res > 0) {
                    logger.debug("Alumno actualizado");
                } else {
                    logger.error("Alumno no actualizado al no encontrarse en la base de datos con id: " + alumno.getId());
                    throw new AlumnoNoEncotradoException("Alumno/a no encontrado con id: " + alumno.getId());
                }
            } catch (SQLException | AlumnoNoEncotradoException e) {
                throw new CompletionException(e);
            }
            return alumno;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Long aLong) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "DELETE FROM ALUMNOS WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                logger.debug("Borrando el alumno con id: " + aLong);
                stmt.setLong(1, aLong);
                var res = stmt.executeUpdate();
                stmt.close();
                //db.closeConnection();
                return res > 0;
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM ALUMNOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                stmt.executeUpdate();
                stmt.close();
                // db.closeConnection();
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

}
