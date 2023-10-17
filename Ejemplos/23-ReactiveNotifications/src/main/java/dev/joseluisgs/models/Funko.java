package dev.joseluisgs.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Funko {
    private final UUID id;
    private final String name;
    private final double price;
}