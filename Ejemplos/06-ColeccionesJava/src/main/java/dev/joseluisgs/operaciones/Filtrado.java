package dev.joseluisgs.operaciones;

import java.util.LinkedHashMap;
import java.util.List;

public class Filtrado {
    public static void main(String[] args) {
        var lista = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        // Todo se hace con la API Stream!!

        // elementos pares
        lista.stream().filter(x -> x % 2 == 0).forEach(System.out::println);

        // elementos impares
        lista.stream().filter(x -> x % 2 != 0).forEach(System.out::println);

        var mapa = new LinkedHashMap<Integer, String>();
        mapa.put(1, "uno");
        mapa.put(2, "dos");
        mapa.put(3, "tres");
        mapa.put(4, "cuatro");

        mapa.entrySet().stream().filter(x -> x.getValue().equals("cuatro")).forEach(System.out::println);
        mapa.entrySet().stream().filter(x -> x.getKey() % 2 == 0).forEach(System.out::println);
    }
}
