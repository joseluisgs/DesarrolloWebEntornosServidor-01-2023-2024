package dev.joseluisgs.common.models;

// Vamos a usar los Records de Java 14

// Lo vamos a hacer todo con String para no liarnos con los Genericos en tiempo de ejecución por red!!!
// Esto quiere decir que el contenido va a en JSON
// Debemos usar GSON dos veces, uno para serializar/deserializar Request y otro para serializar/deserializar el contenido

public record Request(Type type, String content, String token, String createdAt) {
    public enum Type {
        LOGIN, FECHA, UUID, SALIR, OTRO, GETALL, GETBYID, GETBYUUID, POST, UPDATE, DELETE, DELETEALL
    }
}
