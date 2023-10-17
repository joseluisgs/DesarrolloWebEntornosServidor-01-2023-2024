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
    public List<Alumno> findAll() throws SQLException {
        logger.debug("Obteniendo todos los alumnos");
        var query = "SELECT * FROM ALUMNOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            var lista = new ArrayList<Alumno>();
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
            return lista;
        }
    }

    @Override
    public List<Alumno> findByNombre(String nombre) throws SQLException {
        logger.debug("Obteniendo todos los alumnos por nombre que contenga: " + nombre);
        // Vamos a usar Like para buscar por nombre
        String query = "SELECT * FROM ALUMNOS WHERE nombre LIKE ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, "%" + nombre + "%");
            var rs = stmt.executeQuery();
            var lista = new ArrayList<Alumno>();
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
            return lista;
        }
    }

    @Override
    public Optional<Alumno> findById(Long id) throws SQLException {
        logger.debug("Obteniendo el alumno con id: " + id);
        String query = "SELECT * FROM ALUMNOS WHERE id = ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            Optional<Alumno> alumno = Optional.empty();
            while (rs.next()) {
                // Creamos un alumno
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
            return alumno;
        }
    }

    @Override
    public Alumno save(Alumno alumno) throws SQLException, AlumnoNoAlmacenadoException {
        logger.debug("Guardando el alumno: " + alumno);
        String query = "INSERT INTO ALUMNOS (nombre, calificacion, uuid, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        // Vamos a crear los datos de la consultaue necesitamos para insertar automaticos aunque los crea la base de datos
        // alumno.setUuid(UUID.randomUUID());
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            alumno.setCreatedAt(LocalDateTime.now());
            alumno.setUpdatedAt(LocalDateTime.now());
            // importante debemos devolver el alumno con la clave, por eso usamos RETURN_GENERATED_KEYS
            stmt.setString(1, alumno.getNombre());
            stmt.setDouble(2, alumno.getCalificacion());
            stmt.setObject(3, alumno.getUuid());
            stmt.setObject(4, alumno.getCreatedAt());
            stmt.setObject(5, alumno.getUpdatedAt());
            var res = stmt.executeUpdate();
            // Ahora puedo obtener la clave
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
        }
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) throws SQLException, AlumnoNoEncotradoException {
        logger.debug("Actualizando el alumno: " + alumno);
        String query = "UPDATE ALUMNOS SET nombre =?, calificacion =?, updated_at =? WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            // Vamos a crear los datos de la consultaue necesitamos para insertar automaticos aunque los crea la base de datos
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
        }
        return alumno;
    }


    @Override
    public boolean deleteById(Long id) throws SQLException {
        logger.debug("Borrando el alumno con id: " + id);
        String query = "DELETE FROM ALUMNOS WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var res = stmt.executeUpdate();
            return res > 0;
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos los alumnos");
        String query = "DELETE FROM ALUMNOS";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.executeUpdate();
        }
    }
}
