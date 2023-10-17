package dev.joseluisgs.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Carro {
    private List<Producto> productos = new ArrayList<>();

    public Carro() {
        var items = Math.random() * 15 + 1;
        for (int i = 0; i < items; i++) {
            productos.add(new Producto("Producto " + (i + 1)));
        }
    }

    public double getTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }
}