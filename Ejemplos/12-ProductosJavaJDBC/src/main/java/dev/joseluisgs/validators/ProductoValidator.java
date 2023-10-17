package dev.joseluisgs.validators;

import dev.joseluisgs.exceptions.productos.ProductoNoValidoException;
import dev.joseluisgs.models.Producto;

public class ProductoValidator {
    public static void validar(Producto producto) throws ProductoNoValidoException {
        // Early return con excepciones
        if (producto.getNombre().isEmpty()) {
            throw new ProductoNoValidoException("El nombre no puede estar vacío");
        }
        if (producto.getPrecio() < 0) {
            throw new ProductoNoValidoException("El precio no puede ser menor a 0");
        }

        if (producto.getCantidad() < 0) {
            throw new ProductoNoValidoException("La cantidad no puede ser menor a 0");
        }
    }
}
