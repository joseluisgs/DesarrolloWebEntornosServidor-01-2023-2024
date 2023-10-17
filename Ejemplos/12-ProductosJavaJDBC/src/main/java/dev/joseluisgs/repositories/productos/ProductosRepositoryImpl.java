package dev.joseluisgs.repositories.productos;

import dev.joseluisgs.models.Producto;
import dev.joseluisgs.services.DataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductosRepositoryImpl implements ProductosRepository {

    private final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);

    @Override
    public List<Producto> findAll() throws SQLException {
        logger.debug("findAll");
        var productos = new ArrayList<Producto>();
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        var sql = "SELECT * FROM productos";
        // Ejecutamos la consulta
        var rs = db.select(sql).orElseThrow();
        // Recorremos el resultado
        while (rs.next()) {
            // Creamos el objeto
            var producto = new Producto(
                    rs.getLong("id"),
                    UUID.fromString(rs.getObject("uuid").toString()),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    LocalDateTime.parse(rs.getObject("created_at").toString()),
                    LocalDateTime.parse(rs.getObject("updated_at").toString()),
                    rs.getBoolean("disponible")
            );
            // Lo añadimos a la lista
            productos.add(producto);
        }
        // Cerramos la conexión
        db.close();
        // Devolvemos la lista
        return productos;
    }

    @Override
    public Optional<Producto> findById(Long id) throws SQLException {
        logger.debug("findById " + id);
        Optional<Producto> optionalProducto = Optional.empty();
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        var sql = "SELECT * FROM productos WHERE id = ?";
        // Ejecutamos la consulta
        var rs = db.select(sql, id).orElseThrow();
        // Recorremos el resultado
        if (rs.next()) {
            // Creamos el objeto
            var producto = new Producto(
                    rs.getLong("id"),
                    UUID.fromString(rs.getObject("uuid").toString()),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    LocalDateTime.parse(rs.getObject("created_at").toString()),
                    LocalDateTime.parse(rs.getObject("updated_at").toString()),
                    rs.getBoolean("disponible")
            );
            optionalProducto = Optional.of(producto);
        }
        // Cerramos la conexión
        db.close();
        // Devolvemos el objeto
        return optionalProducto;
    }

    @Override
    public Optional<Producto> findByUuid(String uuid) throws SQLException {
        logger.debug("findByUuid " + uuid);
        Optional<Producto> optionalProducto = Optional.empty();
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        var sql = "SELECT * FROM productos WHERE uuid = ?";
        // Ejecutamos la consulta
        var rs = db.select(sql, uuid).orElseThrow();
        // Recorremos el resultado
        if (rs.next()) {
            // Creamos el objeto
            var producto = new Producto(
                    rs.getLong("id"),
                    UUID.fromString(rs.getObject("uuid").toString()),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    LocalDateTime.parse(rs.getObject("created_at").toString()),
                    LocalDateTime.parse(rs.getObject("updated_at").toString()),
                    rs.getBoolean("disponible")
            );
            optionalProducto = Optional.of(producto);
        }
        // Cerramos la conexión
        db.close();
        // Devolvemos el objeto
        return optionalProducto;
    }

    @Override
    public List<Producto> findByNombre(String nombre) throws SQLException {
        logger.debug("findByNombre " + nombre);
        // Si no sabemos montar la consulta con un Like, podemos usar el método filter de la lista sabiendo devolver todo
        // Es decir, con el select all casi lo podemos hacer todo, es encontrar un equilibrio entre oprocesar nosotros las cosas
        // o dejar que la base de datos lo haga
        return findAll().stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    @Override
    public Producto save(Producto entity) throws SQLException {
        var createdTime = LocalDateTime.now();
        var myId = 0L;
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        // El id es autoincremental, por lo que no lo tenemos que pasar por eso usamos el método insertAndGetKey, si no usamos el insert
        // ademas se pasa como null el id, porque no lo tenemos, pero si lo tuvieramos, lo pasariamos
        // cuidado de nuevo con la conversión de datos a los tipos de datos de la base de datos
        // si no, pues podemos pasarle al insert los campos que queramos: insert into tabla (campo1, campo2) values (?, ?)
        var sql = "INSERT INTO productos VALUES (null, ?, ?, ?, ?, ?, ?, ?)";
        // Ejecutamos la consulta
        var rs = db.insertAndGetKey(sql,
                entity.getUuid().toString(),
                entity.getNombre(),
                entity.getPrecio(),
                entity.getCantidad(),
                createdTime.toString(),
                createdTime.toString(),
                entity.isDisponible()
        ).orElseThrow();
        // Recorremos el resultado
        if (rs.next()) {
            // Obtenemos el id
            myId = rs.getLong(1);
        }
        // Cerramos la conexión
        db.close();
        // Devolvemos el objeto
        entity.setId(myId);
        entity.setCreatedAt(createdTime);
        entity.setUpdatedAt(createdTime);
        return entity;
    }

    @Override
    public Producto update(Producto entity) throws SQLException {
        logger.debug("update " + entity);
        var updatedTime = LocalDateTime.now();
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        var sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ?, updated_at = ?, disponible = ? WHERE id = ?";
        var rs = db.update(sql,
                entity.getNombre(),
                entity.getPrecio(),
                entity.getCantidad(),
                updatedTime.toString(),
                entity.isDisponible(),
                entity.getId()
        );
        // Cerramos la conexión
        db.close();
        // Devolvemos el objeto
        entity.setUpdatedAt(updatedTime);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        logger.debug("deleteById " + id);
        // abrimos la conexión
        var db = DataBaseManager.getInstance();
        // Creamos la consulta
        var sql = "DELETE FROM productos WHERE id = ?";
        // Ejecutamos la consulta
        var rs = db.delete(sql, id);
        // Cerramos la conexión
        db.close();
        // Devolvemos el objeto
        return rs == 1;
    }


}
