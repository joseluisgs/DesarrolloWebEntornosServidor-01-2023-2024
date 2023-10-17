package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.List;

public class Agregaciones {
    public static void main(String[] args) {
        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 22),
                new Persona("Jose", 22)
        );

        // contar
        System.out.println(numeros.size());
        System.out.println(
                numeros.stream()
                        .filter(n -> n % 2 == 0)
                        .count()
        );

        // Maximo
        System.out.println(
                numeros.stream()
                        .max(Integer::compareTo).get()
        );

        // Minimo
        System.out.println(
                numeros.stream()
                        .min(Integer::compareTo).get()
        );

        // Suma
        System.out.println(
                numeros.stream()
                        .reduce(0, Integer::sum)
        );

        // average
        System.out.println(
                numeros.stream()
                        .reduce(0, Integer::sum) / (double) numeros.size()
        );


    }
}
