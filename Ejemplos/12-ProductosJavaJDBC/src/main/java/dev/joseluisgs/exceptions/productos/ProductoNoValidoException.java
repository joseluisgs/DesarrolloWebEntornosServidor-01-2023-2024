package dev.joseluisgs.exceptions.productos;

public class ProductoNoValidoException extends ProductoException {
    public ProductoNoValidoException(String message) {
        super("Producto no válido: " + message);
    }
}
