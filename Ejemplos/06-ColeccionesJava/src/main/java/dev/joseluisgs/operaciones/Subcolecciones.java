package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.List;

public class Subcolecciones {
    public static void main(String[] args) {
        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 21),
                new Persona("Jose", 22)
        );

        numeros.stream().takeWhile(numero -> numero < 3).forEach(System.out::println);
        System.out.println();

        personas.stream().takeWhile(persona -> persona.getEdad() < 22).forEach(System.out::println);
        System.out.println();

        numeros.subList(0, 2);

        numeros.stream().skip(2).forEach(System.out::println);
        System.out.println();

        numeros.stream().limit(2).forEach(System.out::println);
        System.out.println();

    }
}
