package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.Comparator;
import java.util.List;

public class Busquedas {
    public static void main(String[] args) {
        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 21),
                new Persona("Jose", 22)
        );

        System.out.println(numeros.indexOf(2));

        var lista = personas.stream()
                .filter(persona -> persona.getNombre().contains("J"))
                .toList();

        var ordenada = lista.stream().sorted().toList();
        var ordenada2 = personas.stream()
                .sorted(Comparator.comparingInt(Persona::getEdad))
                .toList();

    }
}
