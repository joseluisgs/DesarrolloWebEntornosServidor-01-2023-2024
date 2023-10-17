package dev.joseluisgs.repositories.productos;

import dev.joseluisgs.models.Producto;
import dev.joseluisgs.repositories.base.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductosRepository extends CrudRepository<Producto, Long> {
    Optional<Producto> findByUuid(String uuid) throws SQLException;

    List<Producto> findByNombre(String nombre) throws SQLException;
}