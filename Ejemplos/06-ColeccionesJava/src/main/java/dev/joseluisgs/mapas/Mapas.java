package dev.joseluisgs.mapas;

import dev.joseluisgs.models.Persona;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.UUID;

public class Mapas {
    public static void main(String[] args) {
        var mapa = new LinkedHashMap<UUID, Persona>();

        var p1 = new Persona("Pepe", 30);
        var p2 = new Persona("Jose", 20);

        mapa.put(p1.getUuid(), p1);
        mapa.put(p2.getUuid(), p2);

        // lo recorremos
        for (var entry : mapa.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println();

        for (var entry : mapa.values()) {
            System.out.println(entry);
        }

        System.out.println();

        for (var entry : mapa.keySet()) {
            System.out.println(entry);
            System.out.println(mapa.get(entry));
        }

        var mapa2 = new TreeMap<UUID, Persona>(Comparator.naturalOrder());

        mapa2.put(p1.getUuid(), p1);
        mapa2.put(p2.getUuid(), p2);

        for (var entry : mapa2.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
