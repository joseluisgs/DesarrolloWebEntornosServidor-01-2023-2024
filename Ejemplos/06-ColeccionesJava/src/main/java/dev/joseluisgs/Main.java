package dev.joseluisgs;

import dev.joseluisgs.models.Persona;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola API Stream");
        var listaAnimales = new ArrayList<>(List.of("🐶", "🐶", "😺", "🐶", "🐶", "😺"));
        var listaPersonas = new ArrayList<>(List.of(new Persona("Pepe", 18), new Persona("Juan", 20), new Persona("Luis", 17)));

        System.out.println("Recorriendo una lista con For");
        // Para recorrer una lista...
        System.out.println("Lista de animales");
        for (var animal : listaAnimales) {
            System.out.println(animal);
        }
        System.out.println("Lista de Personas");
        for (var persona : listaPersonas) {
            System.out.println(persona);
        }
        System.out.println();

        // Usamos el método forEach para recorrer una lista
        System.out.println("Recorriendo una lista con ForEach");
        System.out.println("Lista de animales");
        listaAnimales.forEach(System.out::println);
        System.out.println("Lista de Personas");
        listaPersonas.forEach(System.out::println);
        System.out.println();

        // Podemos filtrar una lista
        System.out.println("Filtrando una lista con For If");
        System.out.println("Lista filtrada de Animales con If");
        for (var animal : listaAnimales) {
            if (animal.equals("🐶")) {
                System.out.println("🐶");
            }
        }
        System.out.println("Lista filtrada de Personas con If");
        for (var persona : listaPersonas) {
            if (persona.getEdad() > 18) {
                System.out.println(persona);
            }
        }
        System.out.println();

        System.out.println("Filtrando una lista con Filter");
        // Usamos el método filter para filtrar una lista, luego si queremos la recorremos...
        System.out.println("Lista filtrada de Animales con Filter");
        listaAnimales.stream().filter(animal -> animal.equals("🐶")).forEach(System.out::println);
        System.out.println("Lista filtrada de Personas con Filter");
        listaPersonas.stream().filter(persona -> persona.getEdad() > 18).forEach(System.out::println);
        System.out.println();

        // Buscar si existe un objeto con For If
        System.out.println("Buscar un objeto con For If: 🐶");
        var encontrado = false;
        for (var animal : listaAnimales) {
            if (animal.equals("🐶")) {
                encontrado = true;
                break;
            }
        }
        System.out.println("🐶:" + encontrado);
        System.out.println("🐶:" + listaAnimales.contains("🐶"));
        System.out.println();

        System.out.println("Buscar un objeto con For If: nombre es Pepe");
        encontrado = false;
        for (var persona : listaPersonas) {
            if (persona.getNombre().equals("Pepe")) {
                encontrado = true;
                break;
            }
        }
        System.out.println("nombre es Pepe:" + encontrado);
        System.out.println();

        // Buscar si existe un objeto con find
        System.out.println("Buscar un objeto con filter + find o con Match");
        System.out.println("Buscar un objeto con For If: 🐶");
        encontrado = listaAnimales.stream().anyMatch(animal -> animal.equals("🐶"));
        System.out.println("🐶:" + encontrado);
        encontrado = listaPersonas.stream().anyMatch(persona -> persona.getNombre().equals("Pepe"));
        System.out.println("nombre es Pepe:" + encontrado);
        System.out.println();

        // si queremos el objeto encontrado...
        System.out.println("Obteniendo los objetos en base a una condición");
        var perro = listaAnimales.stream().filter(animal -> animal.equals("🐶")).findFirst().orElse(null);
        var pepe = listaPersonas.stream().filter(persona -> persona.getNombre().equals("Pepe")).findFirst().orElse(null);
        System.out.println("🐶:" + perro);
        System.out.println("nombre es Pepe:" + pepe);
        System.out.println();

        // Mapeando objetos, o cambiando datos de objetos
        System.out.println("Mapeando objetos con for e if");
        System.out.println("Lista de si es gatos, cara de sol, si no cara feliz");
        var resGatos = new ArrayList<String>();
        for (String animal : listaAnimales) {
            if (animal.equals("😺")) {
                resGatos.add("😎");
            } else {
                resGatos.add("😁");
            }
        }
        for (var animal : resGatos) {
            System.out.println(animal);
        }
        System.out.println();

        System.out.println("Nombres de personas en mayúsculas");
        var resNombres = new ArrayList<String>();
        for (var persona : listaPersonas) {
            resNombres.add(persona.getNombre().toUpperCase(Locale.getDefault()));
        }
        for (var persona : resNombres) {
            System.out.println(persona);
        }
        System.out.println();

        // Mapeando objetos, o cambiando datos de objetos con Map, o quedándonos con una parte
        System.out.println("Mapeando objetos con Map");
        System.out.println("Lista si es gato Gafas de sol, si no cara feliz");
        var resGatosCaras = listaAnimales.stream()
                .map(animal -> {
                    if (animal.equals("😺")) {
                        return "😎";
                    } else {
                        return "😁";
                    }
                }).toList();


        resGatosCaras.forEach(System.out::println);
        System.out.println();

        System.out.println("Nombres de personas en mayúsculas");
        var resNombresMayusculas = listaPersonas.stream()
                .map(persona -> persona.getNombre()
                        .toUpperCase(Locale.getDefault()))
                .toList();

        resNombresMayusculas.forEach(System.out::println);

        var listaPepeEnMinuscula = listaPersonas.stream()
                // .filter(persona -> persona.getNombre().equals("Pepe"))
                .map(it -> {
                    if (it.getNombre().equals("Pepe")) {
                        return it.getNombre().toLowerCase(Locale.getDefault());
                    } else {
                        return it.getNombre();
                    }
                })
                .collect(Collectors.toList());

        listaPepeEnMinuscula.forEach(System.out::println);
        System.out.println();

        // También puedo ordenar las lista con sort metiendo el comparable sobre la marcha que necesitemos...
        System.out.println("Ordenando lista de personas por nombre");
        var listaPersonasOrdenada = listaPersonas.stream().sorted(Comparator.comparing(Persona::getNombre))
                .collect(Collectors.toList());
        listaPersonasOrdenada.forEach(System.out::println);
        System.out.println();

        System.out.println("Ordenando lista de personas por nombre reverso");
        listaPersonasOrdenada = listaPersonas.stream().sorted(Comparator.comparing(Persona::getNombre).reversed())
                .collect(Collectors.toList());
        listaPersonasOrdenada.forEach(System.out::println);
        System.out.println();

        System.out.println("Ordenando lista de personas por edad");
        listaPersonasOrdenada = listaPersonas.stream().sorted(Comparator.comparing(Persona::getEdad))
                .collect(Collectors.toList());
        listaPersonasOrdenada.forEach(System.out::println);
        System.out.println();

        // También puedo filtrar las lista con filter metiendo la condición sobre la marcha que necesitemos...
        // Ordenar los resultados e imprimirlos.
        // Imagina que es un select: dame el nombre de todas las personas mayores de edad ordenados por edad descendente
        // select nombre from persona where edad > 18 order by edad desc
        System.out.println("Nombre de personas mayores de edad ordenados por edad descendente");
        // Puedo guardar el resultado en una lista o imprimirlo directamente...
        listaPersonas.stream()
                .filter(persona -> persona.getEdad() >= 18)
                .sorted(Comparator.comparing(Persona::getEdad).reversed())
                .forEach(System.out::println);
        System.out.println();
    }
}