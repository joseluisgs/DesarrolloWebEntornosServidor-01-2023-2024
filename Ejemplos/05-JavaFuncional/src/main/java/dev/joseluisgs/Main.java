package dev.joseluisgs;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola Programación Funcional");

        // Tipo funcion suma
        Function<Integer, Integer> incremento = x -> x + 1;
        var resultado = incremento.apply(5);
        System.out.println(resultado);
        System.out.println(incrementar(incremento, 5));
        // Ejemplo con lambda
        System.out.println(incrementar(x -> x + 1, 5));
        System.out.println(incrementar(x -> 2 * x + 5, 5));

        ListaInteger lista = new ListaInteger();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        lista.add(4);

        System.out.println("Filtrado");
        var res1 = lista.filter(x -> x % 2 == 0);
        System.out.println(res1);
        var res2 = lista.filter(x -> x == 4);

        var listaString = new ListaString();
        listaString.add("Hola");
        listaString.add("Adios");
        listaString.add("Hello");
        listaString.add("Bye");

        System.out.println("Filtrado longitud mayor que 4");
        var res3 = listaString.filter(x -> x.length() > 4);
        System.out.println(res3);

        System.out.println("Mapeado a mayúsculas");
        var res4 = listaString.map(x -> x.toUpperCase());
        //var res4 = listaString.map(String::toUpperCase);
        System.out.println(res4);

        System.out.println("Encontrar Hello");
        var res5 = listaString.find(x -> x.equals("Hello"));
        System.out.println(res5);

        System.out.println("Encontrar índice Bye");
        var res6 = listaString.findIndex(x -> x.equals("Bye"));
        System.out.println(res6);

        System.out.println("ForEach");
        listaString.forEach(x -> {
            System.out.println(x);
            return null;
        });

        System.out.println("Ordenacion en base a la longitud");
        listaString.sort(x);

    }

    public static int incrementar(Function<Integer, Integer> funcion, int a) {
        return funcion.apply(a);
    }
}