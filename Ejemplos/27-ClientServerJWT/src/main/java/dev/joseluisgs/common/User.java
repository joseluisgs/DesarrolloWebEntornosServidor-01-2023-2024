package dev.joseluisgs.common;

public record User(long id, String username, String password, Role role) {
    public enum Role {
        ADMIN, USER
    }
}