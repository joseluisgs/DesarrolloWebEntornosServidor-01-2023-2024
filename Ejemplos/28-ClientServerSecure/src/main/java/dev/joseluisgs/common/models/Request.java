package dev.joseluisgs.common.models;

// Vamos a usar los Records de Java 14
/*
@Data
public class Request<T> {

    private final Type type;
    private final T content;
    private final LocalDateTime createdAt;
    // Tipo de petición
    public enum Type {
        LOGIN, FECHA, UUID, SALIR, OTRO
    }

}*/

public record Request<T>(Type type, T content, String token, String createdAt) {
    public enum Type {
        LOGIN, FECHA, UUID, SALIR, OTRO
    }
}
