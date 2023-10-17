package dev.joseluisgs;

import dev.joseluisgs.controllers.ProductosController;
import dev.joseluisgs.exceptions.productos.ProductoNoEncontradoException;
import dev.joseluisgs.exceptions.productos.ProductoNoValidoException;
import dev.joseluisgs.factories.ProductosFactory;
import dev.joseluisgs.models.Producto;
import dev.joseluisgs.repositories.productos.ProductosRepositoryImpl;
import dev.joseluisgs.services.DataBaseManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException, ProductoNoValidoException, ProductoNoEncontradoException {
        System.out.println("Hola Bases de Datos!");

        initDataBase();

        ejemploControlador();


        // ejemploRepository();
    }

    private static void ejemploControlador() throws SQLException, ProductoNoValidoException, ProductoNoEncontradoException {
        var productosController = new ProductosController(new ProductosRepositoryImpl());

        for (int i = 0; i < 10; i++) {
            productosController.save(ProductosFactory.productoRandom());
        }

        System.out.println("Todos los productos:");
        productosController.findAll().forEach(System.out::println);

        System.out.println();
        System.out.println("Producto NO disponibles");
        productosController.findAllDisponible(false).forEach(System.out::println);

        System.out.println();
        System.out.println("Producto con id 1");
        System.out.println(productosController.findById(1L));

        System.out.println();
        System.out.println("Producto con uuid 5f5e58ec-e099-4ef6-ac49-ebde498c913e");
        System.out.println(productosController.findByUuid("5f5e58ec-e099-4ef6-ac49-ebde498c913e"));

        System.out.println();
        System.out.println("Producto con nombre 'Prod'");
        productosController.findByNombre("Prod").forEach(System.out::println);

        System.out.println();
        System.out.println("Producto que no existe");
        try {
            System.out.println(productosController.findById(-100L));
        } catch (ProductoNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Actualizando un producto 1");
        var productoUpdate = new Producto("Producto Update", 100.0, 10);
        productoUpdate.setId(1L);
        System.out.println(productosController.update(productoUpdate));

        System.out.println();
        System.out.println("Eliminando un producto 1");
        productosController.deleteById(1L);

        System.out.println();
        System.out.println("Todos los productos:");
        productosController.findAll().forEach(producto -> System.out.println(producto.toLocaleString()));

        // Probamos las excepciones
        System.out.println();
        System.out.println("Buscar producto -999");
        try {
            System.out.println(productosController.findById(-999L));
        } catch (ProductoNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Borrar producto -999");
        try {
            productosController.deleteById(-999L);
        } catch (ProductoNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Insertar/actualizar producto con campos no validados");
        try {
            var productoUpdate2 = new Producto("Producto Update", 100.0, -1);
            productoUpdate2.setId(3L);
            productosController.update(productoUpdate2);
        } catch (ProductoNoValidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    private static void ejemploRepository() throws SQLException {
        var productosRepository = new ProductosRepositoryImpl();

        System.out.println("Todos los productos:");
        productosRepository.findAll().forEach(System.out::println);

        System.out.println();
        System.out.println("Producto con id 1:");
        var producto = productosRepository.findById(1L);
        System.out.println(producto.orElseThrow());

        System.out.println();
        System.out.println("Producto con uuid 5f5e58ec-e099-4ef6-ac49-ebde498c913e:");
        var producto2 = productosRepository.findByUuid("5f5e58ec-e099-4ef6-ac49-ebde498c913e");
        System.out.println(producto2.orElseThrow());

        System.out.println();
        System.out.println("Producto con nombre 'Prod':");
        var producto3 = productosRepository.findByNombre("Prod");
        producto3.forEach(System.out::println);

        System.out.println();
        System.out.println("Insertando un producto:");
        var producto4 = new Producto("Producto Insert", 100.0, 10);
        var producto4Insertado = productosRepository.save(producto4);
        System.out.println(producto4Insertado);

        System.out.println();
        System.out.println("Actualizando un producto:");
        producto4Insertado.setNombre("Producto Updated ");
        var producto4Actualizado = productosRepository.update(producto4Insertado);
        System.out.println(producto4Actualizado);

        System.out.println();
        System.out.println("Eliminando un producto:");
        productosRepository.deleteById(producto4Actualizado.getId());
        System.out.println("Producto eliminado");

        System.out.println("Todos los productos:");
        productosRepository.findAll().forEach(System.out::println);


    }

    private static void initDataBase() {
        var propsFile = ClassLoader.getSystemResource("config.properties").getFile();
        var props = new Properties();
        try {
            props.load(new FileInputStream(propsFile));
            var initDataBase = Boolean.parseBoolean(props.getProperty("database.initDatabase", "false"));
            if (initDataBase) {
                System.out.println("Inicializando la base de datos");
                var dbManager = DataBaseManager.getInstance();
                var scriptFile = ClassLoader.getSystemResource("init.sql").getFile();
                dbManager.initData(scriptFile, false);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}