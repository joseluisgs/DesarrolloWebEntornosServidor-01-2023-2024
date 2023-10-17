package dev.joseluisgs.models;

import lombok.Data;

@Data
public class Producto {
    private final String nombre;
    private final int precio = (int) (Math.random() * 10) + 1;
}
