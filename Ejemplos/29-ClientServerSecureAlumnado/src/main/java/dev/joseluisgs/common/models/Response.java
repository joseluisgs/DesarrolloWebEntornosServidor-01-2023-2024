package dev.joseluisgs.common.models;

// Lo vamos a hacer todo con String para no liarnos con los Genericos en tiempo de ejecución por red!!!
// Esto quiere decir que el contenido va a en JSON
// Debemos usar GSON dos veces, uno para serializar/deserializar Request y otro para serializar/deserializar el contenido

public record Response(Status status, String content, String createdAt) {
    public enum Status {
        OK, ERROR, BYE, TOKEN
    }
}
