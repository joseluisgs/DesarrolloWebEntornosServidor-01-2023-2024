package dev.joseluisgs.aula;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ConsultasAlumnos {
    public static void main(String[] args) {

        Optional<Integer> numero = Optional.empty();

        var alumnado = List.of(
                new Alumno("Juan", 7.5, "DAM"),
                new Alumno("Pedro", 8.5, "DAM"),
                new Alumno("Ana", 9.5, "DAW"),
                new Alumno("María", 8.5, "DAM"),
                new Alumno("José", 9.5, "DAW"),
                new Alumno("Alicia", 7.5, "DAW")
        );

        // imprimimos los alumnos
        alumnado.forEach(System.out::println);
        System.out.println();

        // Alumnos de DAM
        System.out.println("Alumnos de DAM");
        alumnado.stream().filter(a -> a.getCurso().equals("DAM")).forEach(System.out::println);
        System.out.println();

        // Alumnos con nota >= 8.5
        System.out.println("Alumnos con nota >= 8.5");
        alumnado.stream().filter(a -> a.getNota() >= 8.5).forEach(System.out::println);
        System.out.println();

        // Lista de Alumnos de DAM con nota >= 8.5
        System.out.println("Lista de Alumnos de DAM con nota >= 8.5");
        alumnado.stream()
                .filter(a -> a.getCurso().equals("DAM") && a.getNota() >= 8.5)
                .forEach(System.out::println);
        System.out.println();

        // Alumnos con nota máxima
        System.out.println(alumnado.stream().max(Comparator.comparingDouble(Alumno::getNota)).get());

        // Alumnos con nota mínima
        System.out.println(alumnado.stream().min(Comparator.comparingDouble(Alumno::getNota)).get());

        // Nota media de los alumnos de DAM
        System.out.println("Nota media de los alumnos de DAM");
       /* var medias = alumnado.stream().filter(a -> a.getCurso().equals("DAM"))
                .map(Alumno::getNota).toList();
        var notaMedia = medias.stream().reduce(0.0, Double::sum) / medias.size();
        System.out.println(notaMedia);*/
        var medias = alumnado.stream().filter(a -> a.getCurso().equals("DAM"))
                .mapToDouble(Alumno::getNota).average();
        System.out.println(medias);

        // Alumnado agrupado por grupo
        System.out.println("Alumnado agrupado por grupo");
        alumnado.stream().collect(
                groupingBy(Alumno::getCurso)
        ).forEach((grupo, alumnos) -> {
            System.out.println(grupo);
            alumnos.forEach(System.out::println);
        });
        System.out.println();

        // Alumnado agrupado por nota mayor que 8.5
        alumnado.stream().collect(groupingBy(a -> a.getNota() > 8.5 ? "Sobresalientes" : "Otros"))
                .forEach((grupo, alumnos) -> {
                    System.out.println(grupo);
                    alumnos.forEach(System.out::println);
                });
        System.out.println();

        // Alumnado ordenado por nota descendente
        System.out.println("Alumnado ordenado por nota descendente");
        alumnado.stream().sorted(Comparator.comparingDouble(Alumno::getNota).reversed())
                .forEach(System.out::println);

        System.out.println();

        // Buscar alumnos con nota >= 8.5
        System.out.println(
                alumnado.stream().filter(a -> a.getNota() >= 8.5)
                        .findFirst().get()
        );
        System.out.println();

        // Buscar alumnos con nota >= 8.5 y curso DAW
        System.out.println(
                alumnado.stream().filter(a -> a.getNota() >= 8.5 && a.getCurso().equals("DAW"))
                        .findFirst().get()
        );
        System.out.println();

        // Numero de alumnos agrupados por nota mayor que 8.5
        System.out.println("Numero de alumnos agrupados por nota mayor que 8.5");
        System.out.println(
                alumnado.stream().filter(a -> a.getNota() >= 8.5)
                        .collect(Collectors.groupingBy(Alumno::getNota, Collectors.counting()))
        );
        System.out.println();

        // Numero de alumnos agrupados por nota mayor que 8.5
        var notaMayor = alumnado.stream().filter(a -> a.getNota() >= 8.5);
        var notaMenor = alumnado.stream().filter(a -> a.getNota() < 8.5);
        var numAlumnos = new LinkedHashMap<String, Long>();
        numAlumnos.put("Sobresalientes", notaMayor.count());
        numAlumnos.put("Otros", notaMenor.count());

        System.out.println(numAlumnos);

    }

}
