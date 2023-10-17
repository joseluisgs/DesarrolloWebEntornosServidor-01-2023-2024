package dev.joseluisgs.exceptions.productos;

public class ProductoNoGuardadoException extends ProductoException {
    public ProductoNoGuardadoException(String message) {
        super("Producto no guardado: " + message);
    }
}
