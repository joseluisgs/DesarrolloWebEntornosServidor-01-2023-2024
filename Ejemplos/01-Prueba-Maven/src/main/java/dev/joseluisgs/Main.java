package dev.joseluisgs;

import dev.joseluisgs.models.Persona;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola Maven");
        Persona p1 = new Persona("Pedro", 20);
        System.out.println(p1);
        p1.setNombre("Ana");
        p1.setEdad(30);
        System.out.println(p1);
    }
}