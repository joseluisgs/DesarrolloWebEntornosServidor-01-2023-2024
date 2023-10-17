package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.List;

public class Mapeado {
    public static void main(String[] args) {
        var lista = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        lista.stream().map(i -> i * 2).forEach(System.out::println);

        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 21),
                new Persona("Jose", 22)
        );

        personas.stream().map(Persona::getNombre).forEach(System.out::println);
        personas.stream()
                .filter(p -> p.getEdad() > 20)
                .map(Persona::getNombre)
                .forEach(System.out::println);

    }
}
