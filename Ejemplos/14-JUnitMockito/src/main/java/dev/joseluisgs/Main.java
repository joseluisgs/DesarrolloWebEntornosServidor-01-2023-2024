package dev.joseluisgs;

import dev.joseluisgs.calculadora.Calculadora;
import dev.joseluisgs.calculadora.CalculadoraService;

public class Main {
    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora();
        System.out.println("Suma: " + calculadora.sumar(2, 3));
        System.out.println("Resta: " + calculadora.restar(2, 3));
        System.out.println("Multiplicación: " + calculadora.multiplicar(2, 3));
        try {
            System.out.println("División: " + calculadora.dividir(2, 3));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Ahora dividimos por cero
        try {
            System.out.println("División: " + calculadora.dividir(2, 0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Ahora con el servicio
        CalculadoraService calculadoraService = new CalculadoraService(calculadora);
        System.out.println("Suma: " + calculadoraService.sumar(2, 3));
        System.out.println("Resta: " + calculadoraService.restar(2, 3));
        System.out.println("Multiplicación: " + calculadoraService.multiplicar(2, 3));
        try {
            System.out.println("División: " + calculadoraService.dividir(2, 3));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Ahora dividimos por cero
        try {
            System.out.println("División: " + calculadoraService.dividir(2, 0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}