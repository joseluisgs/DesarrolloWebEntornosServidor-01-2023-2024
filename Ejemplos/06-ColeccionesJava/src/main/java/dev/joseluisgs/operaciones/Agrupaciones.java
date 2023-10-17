package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class Agrupaciones {
    public static void main(String[] args) {
        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 22),
                new Persona("Jose", 22)
        );

        // Agrupamos por edad
        var personasPorEdad = personas.stream()
                .collect(groupingBy(Persona::getEdad));

        for (var persona : personasPorEdad.entrySet()) {
            System.out.println(persona.getKey() + ": " + persona.getValue());
        }
        System.out.println();

        // Agrupamos por pares e impares
        var numerosPares = numeros.stream()
                .collect(groupingBy(n -> n % 2 == 0 ? "Pares" : "Impares"));

        for (var numero : numerosPares.entrySet()) {
            System.out.println(numero.getKey() + ": " + numero.getValue());
        }
        System.out.println();

    }
}

