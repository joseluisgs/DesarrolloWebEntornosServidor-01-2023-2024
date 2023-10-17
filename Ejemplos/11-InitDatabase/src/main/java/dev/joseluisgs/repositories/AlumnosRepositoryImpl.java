package dev.joseluisgs.repositories;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Esta es la clase que se encarga de la persistencia de los alumnos
public class AlumnosRepositoryImpl implements AlumnosRepository {
    // Singleton
    private static AlumnosRepositoryImpl instance;
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
    public List<Alumno> findByNombre(String nombre) throws SQLException {
        // Vamos a usar Like para buscar por nombre
        var lista = new ArrayList<Alumno>();
        String query = "SELECT * FROM ALUMNOS WHERE nombre LIKE ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, "%" + nombre + "%");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                // Creamos un alumno
                var alumno = new Alumno(
                        rs.getLong("id"),
                        rs.getString("nombre")
                );
                // Lo añadimos a la lista
                lista.add(alumno);
            }
        }
        return lista;
    }

    @Override
    public Alumno save(Alumno alumno) throws SQLException {
        // importante debemos devolver el alumno con la clave, por eso usamos RETURN_GENERATED_KEYS
        String query = "INSERT INTO alumnos (nombre) VALUES (?)";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, alumno.getNombre());
            var res = stmt.executeUpdate();
            // Ahora puedo obtener la clave
            if (res > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                while (rs.next()) {
                    // Asignamos la clave al alumno
                    alumno.setId(rs.getLong(1));
                }
                rs.close();
            }
        }
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) throws SQLException {
        String query = "UPDATE alumnos SET nombre =? WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, alumno.getNombre());
            stmt.setLong(2, alumno.getId());
            stmt.executeUpdate();
        }
        return alumno;
    }

    @Override
    public Optional<Alumno> findById(Long aLong) throws SQLException {
        String query = "SELECT * FROM alumnos WHERE id = ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, aLong);
            var rs = stmt.executeQuery();
            Optional<Alumno> alumno = Optional.empty();
            while (rs.next()) {
                // Creamos un alumno
                alumno = Optional.of(new Alumno(
                        rs.getLong("id"),
                        rs.getString("nombre")
                ));
            }
            rs.close();
            return alumno;
        }
    }

    @Override
    public List<Alumno> findAll() throws SQLException {
        String query = "SELECT * FROM alumnos";
        var lista = new ArrayList<Alumno>();
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            while (rs.next()) {
                // Creamos un alumno
                var alumno = new Alumno(
                        rs.getLong("id"),
                        rs.getString("nombre")
                );
                // Lo añadimos a la lista
                lista.add(alumno);
            }
            rs.close();

        }
        return lista;
    }

    @Override
    public boolean deleteById(Long aLong) throws SQLException {
        String query = "DELETE FROM alumnos WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, aLong);
            var res = stmt.executeUpdate();
            return res > 0;
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        String query = "DELETE FROM alumnos";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.executeUpdate();
        }
    }
}
