package dev.joseluisgs.models;

import dev.joseluisgs.locale.MyLocale;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Producto {
    private long id;
    private UUID uuid = UUID.randomUUID();
    private String nombre;
    private double precio;
    private int cantidad;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean disponible = true;

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String toLocaleString() {
        return "Producto{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", nombre='" + nombre + '\'' +
                ", precio=" + MyLocale.toLocalMoney(precio) +
                ", cantidad=" + cantidad +
                ", createdAt=" + MyLocale.toLocalDateTime(createdAt) +
                ", updatedAt=" + MyLocale.toLocalDateTime(updatedAt) +
                ", disponible=" + disponible +
                '}';
    }
}
