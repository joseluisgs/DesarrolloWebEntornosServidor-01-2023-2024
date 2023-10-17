package dev.joseluisgs;

import dev.joseluisgs.utils.Calculadora;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        System.out.println("Hola Docker");
        Calculadora calculadora = new Calculadora();
        System.out.println("Suma: " + calculadora.suma(2, 3));
        System.out.println("Resta: " + calculadora.resta(2, 3));
        System.out.println("Multiplicación: " + calculadora.multiplicacion(2, 3));
        System.out.println("División: " + calculadora.division(2, 3));
    }
}