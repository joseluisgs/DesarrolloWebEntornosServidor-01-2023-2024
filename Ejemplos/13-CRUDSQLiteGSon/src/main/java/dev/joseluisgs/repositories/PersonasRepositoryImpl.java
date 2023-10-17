package dev.joseluisgs.repositories;

import dev.joseluisgs.managers.DataBaseManager;
import dev.joseluisgs.models.Persona;
import dev.joseluisgs.services.BackupJson;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonasRepositoryImpl implements PersonasRepository {
    private final DataBaseManager dataBase;
    private final BackupJson backup;

    public PersonasRepositoryImpl(DataBaseManager dataBase, BackupJson backup) {
        this.dataBase = dataBase;
        this.backup = backup;
    }

    @Override
    public List<Persona> findAll() throws SQLException {
        // Buscamos todos en la BD.
        var sql = "SELECT * FROM personas";
        dataBase.open();
        var res = dataBase.select(sql).orElseThrow(() -> new SQLException("Error al obtener las personas."));
        // Creamos una lista de personas.
        var personas = new ArrayList<Persona>();
        // Recorremos el resultado.
        while (res.next()) {
            // Creamos una persona.
            var persona = Persona.builder()
                    .id(res.getInt("id"))
                    .uuid(UUID.fromString(res.getString("uuid")))
                    .nombre(res.getString("nombre"))
                    .edad(res.getInt("edad"))
                    .createdAt(LocalDate.parse(res.getString("createdAt")))
                    .build();
            personas.add(persona);
        }
        dataBase.close();
        return personas;
    }

    @Override
    public Optional<Persona> findById(Integer id) throws SQLException {
        var sql = "SELECT * FROM personas WHERE id = ?";
        dataBase.open();
        var res = dataBase.select(sql, id).orElseThrow(() -> new SQLException("Error al obtener la persona con id: " + id));
        if (res.next()) {
            var persona = Persona.builder()
                    .id(res.getInt("id"))
                    .uuid(UUID.fromString(res.getString("uuid")))
                    .nombre(res.getString("nombre"))
                    .edad(res.getInt("edad"))
                    .createdAt(LocalDate.parse(res.getString("createdAt")))
                    .build();
            dataBase.close();
            return Optional.of(persona);
        }
        dataBase.close();
        return Optional.empty();
    }

    @Override
    public Persona save(Persona persona) throws SQLException {
        // Salvamos en la BBDD. // si no ponemos los campos, hay que poner eb values null, como priemr campo por el id
        var sql = "INSERT INTO personas (uuid, nombre, edad, createdAt) VALUES (?, ?, ?, ?)";
        dataBase.open();
        var res = dataBase.insert(sql, persona.getUuid(), persona.getNombre(), persona.getEdad(), persona.getCreatedAt())
                .orElseThrow(() -> new SQLException("Error al insertar la persona."));
        // Le cogemos el id que nos ha dado la BBDD.
        if (res.next()) {
            persona.setId(res.getInt(1));
            dataBase.close();
        } else {
            dataBase.close();
            throw new SQLException("Error al obtener el id de la persona.");
        }
        return persona;
    }

    @Override
    public Persona update(Integer id, Persona persona) throws SQLException {
        var sql = "UPDATE personas SET uuid = ?, nombre = ?, edad = ?, createdAt = ? WHERE id = ?";
        dataBase.open();
        var res = dataBase.update(sql, persona.getUuid(), persona.getNombre(), persona.getEdad(), persona.getCreatedAt(), id);
        dataBase.close();
        if (res > 0) {
            return persona;
        } else {
            throw new SQLException("Error al actualizar la persona con id: " + id);
        }
    }

    @Override
    public Persona delete(Integer id) throws SQLException {
        var persona = findById(id).orElseThrow(() -> new SQLException("Error al borrar la persona con id: " + id));
        var sql = "DELETE FROM personas WHERE id = ?";
        dataBase.open();
        var res = dataBase.delete(sql, id);
        dataBase.close();
        if (res > 0) {
            return persona;
        } else {
            throw new SQLException("Error al borrar la persona con id: " + id);
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM personas";
        dataBase.open();
        dataBase.delete(sql);
        dataBase.close();
    }

    public void backup() throws SQLException, IOException {
        backup.backup(this.findAll());
    }

    public void restore() throws SQLException, IOException {
        var personas = backup.restore();
        this.deleteAll();
        for (var persona : personas) {
            this.save(persona);
        }
    }
}
