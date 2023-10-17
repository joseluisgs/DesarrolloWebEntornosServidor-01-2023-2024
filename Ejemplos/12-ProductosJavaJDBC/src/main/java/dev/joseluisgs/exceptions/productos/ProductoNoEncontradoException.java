package dev.joseluisgs.exceptions.productos;

public class ProductoNoEncontradoException extends ProductoException {
    public ProductoNoEncontradoException(String message) {
        super("Producto no encontrado: " + message);
    }
}
