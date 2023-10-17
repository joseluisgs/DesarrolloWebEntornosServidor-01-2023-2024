package dev.joseluisgs.models;

// Vamos a usar los records de Java 14
/*
@Data
public class Response<T> {
    public enum Status {
        OK, ERROR
    }

    private final Status status;
    private final T content;
    private final String createdAt;
}*/

public record Response<T>(Status status, T content, String createdAt) {
    public enum Status {
        OK, ERROR, BYE
    }
}