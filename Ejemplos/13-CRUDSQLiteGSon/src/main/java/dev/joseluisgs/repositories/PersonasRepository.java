package dev.joseluisgs.repositories;

import dev.joseluisgs.models.Persona;

import java.io.IOException;
import java.sql.SQLException;

public interface PersonasRepository extends CRUDRepository<Persona, Integer> {
    void deleteAll() throws SQLException;

    void backup() throws SQLException, IOException;

    void restore() throws SQLException, IOException;

}
