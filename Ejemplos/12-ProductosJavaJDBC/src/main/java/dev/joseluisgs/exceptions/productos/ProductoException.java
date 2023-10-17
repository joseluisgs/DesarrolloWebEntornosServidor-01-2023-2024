package dev.joseluisgs.exceptions.productos;

abstract class ProductoException extends Exception {
    public ProductoException(String message) {
        super(message);
    }
}

