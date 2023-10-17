package dev.joseluisgs;

import dev.joseluisgs.model.Cancion;

import java.util.*;
import java.util.stream.Collectors;

public class ConsultasCanciones {
    List<Cancion> canciones;

    public ConsultasCanciones() {
        // Cargamos la lista de productos
        canciones = new ArrayList<>(
                Arrays.asList(
                        new Cancion("Livin' on Prayer", "Bon Jovi"),
                        new Cancion("Long Hot Summer", "Keith Urban"),
                        new Cancion("It's my Life", "Bon Jovi"),
                        new Cancion("Dolor Fantasma", "Amadeus"),
                        new Cancion("Run To You", "Bryan Adams"),
                        new Cancion("Summer of 69", "Bryna Adams"),
                        new Cancion("Paranoid", "Black Sabbath"),
                        new Cancion("Cherokee", "Europe"),
                        new Cancion("River Bank", "Brad Paisley")
                )
        );
        procesarStreams();
    }

    private void procesarStreams() {
        filtradoDatos();

        filtradoStream();

        realizandoTranformacion();

        cancionesPorCantante();

        quitandoDuplicados();

        contandoElementos();

        agrupandoPorCantante();


    }

    private void filtradoDatos() {
        // Filtrando elementos forma tradicional
        for (Cancion cancion : filtrarCantante(canciones, "Bon Jovi")) {
            System.out.println("Tradicional: " + cancion);
        }
    }

    private void filtradoStream() {
        // Filtrando elementos con Stream
        List<Cancion> listadoCanciones = filtrarCantanteStream(canciones, "Bon Jovi");
        listadoCanciones.forEach(c -> System.out.println("Stream: " + c));
    }

    private void cancionesPorCantante() {
        List<String> listadoTitulos = obtenerCancionesPorCantante(canciones, "Bon Jovi");
    }


    public List<Cancion> filtrarCantante(List<Cancion> canciones, String cantante) {
        List<Cancion> listaFiltrada = new LinkedList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getCantante().equals(cantante))
                listaFiltrada.add(cancion);
        }
        return listaFiltrada;
    }

    private void realizandoTranformacion() {
        List<Cancion> listadoCanciones;

        listadoCanciones = filtrarCantanteMayuscula(canciones, "Bon Jovi");
        listadoCanciones.forEach(c -> System.out.println("Stream Map: " + c));
    }

    public List<Cancion> filtrarCantanteStream(List<Cancion> canciones, String cantante) {
        return canciones.stream()
                .filter(c -> c.getCantante().equals(cantante))
                .collect(Collectors.toList());
    }

    public List<Cancion> filtrarCantanteMayuscula(List<Cancion> canciones, String cantante) {
        return canciones.stream()
                .filter(c -> c.getCantante().equals(cantante))
                .map(c -> {
                            c.setCantante(c.getCantante().toUpperCase());
                            return c;
                        }
                )
                .collect(Collectors.toList());
    }

    public List<String> obtenerCancionesPorCantante(List<Cancion> canciones, String cantante) {
        return canciones.stream()
                .filter(c -> c.getCantante().equals(cantante))
                .map(c -> c.getTitulo().toUpperCase())
                .collect(Collectors.toList());
    }

    private void quitandoDuplicados() {
        //Esta canción ya existe pero la agregaremos nuevamente
        canciones.add(new Cancion("Summer of 69", "Bryan Adams"));
        // aplicamos el distinct
        canciones.stream().distinct().forEach(System.out::println);
    }

    private void contandoElementos() {
        // Contando elementos
        long count = canciones.stream()
                .filter(c -> c.getCantante().equalsIgnoreCase("Bon Jovi"))
                .count();
        System.out.println("Bon Jovi aparece: " + count + " veces");
    }

    private void agrupandoPorCantante() {
        Map<String, Long> counted = canciones.stream().collect(
                Collectors.groupingBy(
                        c -> c.getCantante(), Collectors.counting()
                )
        );
        System.out.println(counted);
    }
}
