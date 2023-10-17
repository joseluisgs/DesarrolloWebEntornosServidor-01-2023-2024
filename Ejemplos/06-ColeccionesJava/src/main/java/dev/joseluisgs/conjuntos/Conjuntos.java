package dev.joseluisgs.conjuntos;

import dev.joseluisgs.models.Persona;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class Conjuntos {
    public static void main(String[] args) {
        var conj1 = new LinkedHashSet<String>();

        conj1.add("A");
        conj1.add("B");
        conj1.add("C");
        conj1.add("D");
        conj1.add("A");
        conj1.add("B");

        // lo recorremos
        for (String s : conj1) {
            System.out.println(s);
        }
        System.out.println();

        var conj2 = new LinkedHashSet<Persona>();

        var p1 = new Persona("Pepe", 30);
        var p2 = new Persona("Jose", 20);

        conj2.add(p1);
        conj2.add(p2);
        conj2.add(p1);

        // lo recorremos
        for (Persona p : conj2) {
            System.out.println(p);
        }
        System.out.println();

        var conj3 = new TreeSet<Persona>();
        conj3.add(p1);
        conj3.add(p2);
        conj3.add(p1);

        // lo recorremos
        for (Persona p : conj3) {
            System.out.println(p);
        }
        System.out.println();

        // var conj4 = new TreeSet<Persona>((Persona o1, Persona o2) -> o1.getEdad() - o2.getEdad());
        var conj4 = new TreeSet<Persona>(Comparator.comparingInt(Persona::getEdad));
        conj4.add(p1);
        conj4.add(p2);
        conj4.add(p1);

        // lo recorremos
        for (Persona p : conj4) {
            System.out.println(p);
        }
        System.out.println();
    }
}
