package dev.joseluisgs.models;

import lombok.Data;

@Data
public class Cliente {
    private final String nombre;
    private final Carro carro = new Carro();
}
