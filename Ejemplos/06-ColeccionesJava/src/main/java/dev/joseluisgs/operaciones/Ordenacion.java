package dev.joseluisgs.operaciones;

import dev.joseluisgs.models.Persona;

import java.util.List;

public class Ordenacion {
    public static void main(String[] args) {
        var numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        var personas = List.of(
                new Persona("Jose", 20),
                new Persona("Juan", 21),
                new Persona("Jose", 22)
        );

        // ordenacion por nombre
        personas.sort((persona1, persona2) -> persona1.getNombre().compareTo(persona2.getNombre()));
        System.out.println(personas);

        // ordenacion por edad
        personas.sort((persona1, persona2) -> persona1.getEdad() - persona2.getEdad());
        System.out.println(personas);

    }
}
