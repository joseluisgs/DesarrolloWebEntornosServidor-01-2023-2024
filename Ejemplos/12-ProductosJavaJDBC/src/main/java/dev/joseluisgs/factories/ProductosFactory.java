package dev.joseluisgs.factories;

import dev.joseluisgs.models.Producto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductosFactory {
    public static Producto productoRandom() {
        return new Producto(
                0,
                UUID.randomUUID(),
                "Producto " + UUID.randomUUID().toString().substring(0, 5),
                Math.random() * 100,
                (int) (Math.random() * 50),
                LocalDateTime.now(),
                LocalDateTime.now(),
                // 50% de posibilidades de que esté disponible
                Math.random() < 0.5
        );
    }
}
