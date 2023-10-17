package dev.joseluisgs.controllers;

import dev.joseluisgs.exceptions.productos.ProductoNoEncontradoException;
import dev.joseluisgs.exceptions.productos.ProductoNoValidoException;
import dev.joseluisgs.models.Producto;
import dev.joseluisgs.repositories.productos.ProductosRepository;
import dev.joseluisgs.services.DataBaseManager;
import dev.joseluisgs.validators.ProductoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ProductosController {
    private final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);

    private final ProductosRepository productosRepository;

    public ProductosController(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public List<Producto> findAll() throws SQLException {
        logger.debug("findAll");
        return productosRepository.findAll();
    }

    public List<Producto> findAllDisponible(boolean disponible) throws SQLException {
        logger.debug("findAll" + disponible);
        return productosRepository.findAll().stream()
                .filter(producto -> producto.isDisponible() == disponible)
                .toList();
    }

    public Producto findById(Long id) throws SQLException, ProductoNoEncontradoException {
        logger.debug("findById " + id);
        return productosRepository.findById(id).orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con id " + id));
    }

    public List<Producto> findByNombre(String nombre) throws SQLException {
        logger.debug("findByNombre " + nombre);
        return productosRepository.findByNombre(nombre);
    }

    public Producto findByUuid(String uuid) throws SQLException, ProductoNoEncontradoException {
        logger.debug("findByUuid " + uuid);
        return productosRepository.findByUuid(uuid).orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con uuid " + uuid));
    }

    public Producto save(Producto producto) throws SQLException, ProductoNoValidoException {
        logger.debug("save " + producto);
        ProductoValidator.validar(producto);
        return productosRepository.save(producto);
    }

    public Producto update(Producto producto) throws SQLException, ProductoNoValidoException, ProductoNoEncontradoException {
        logger.debug("update " + producto);
        productosRepository.findById(producto.getId()).orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con id " + producto.getId()));
        ProductoValidator.validar(producto);
        return productosRepository.update(producto);
    }

    public Producto deleteById(Long id) throws SQLException, ProductoNoEncontradoException {
        logger.debug("delete " + id);
        var producto = productosRepository.findById(id).orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con id " + id));
        productosRepository.deleteById(id);
        return producto;
    }
}
