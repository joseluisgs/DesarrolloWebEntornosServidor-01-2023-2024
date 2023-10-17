package dev.joseluisgs.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculadoraTest {

    @Test
    void suma() {
        Calculadora calc = new Calculadora();
        int resultado = calc.suma(2, 3);
        Assertions.assertEquals(5, resultado);
    }

    @Test
    void resta() {
        Calculadora calc = new Calculadora();
        int resultado = calc.resta(5, 2);
        Assertions.assertEquals(3, resultado);
    }

    @Test
    void multiplicacion() {
        Calculadora calc = new Calculadora();
        int resultado = calc.multiplicacion(5, 2);
        Assertions.assertEquals(10, resultado);
    }

    @Test
    void division() {
        Calculadora calc = new Calculadora();
        int resultado = calc.division(10, 2);
        Assertions.assertEquals(5, resultado);
    }

    @Test
    void divisionConCero() {
        Calculadora calc = new Calculadora();
        Assertions.assertThrows(ArithmeticException.class, () -> calc.division(10, 0));
    }
}