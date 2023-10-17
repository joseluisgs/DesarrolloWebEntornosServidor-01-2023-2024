package dev.joseluisgs.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    Calculadora calculadora = new Calculadora(); //

    // Antes de cada test
    @BeforeEach
    void setUp() {
        calculadora = new Calculadora();
    }

    @Test
    void sumar() {
        assertAll("Suma",
                () -> assertEquals(5, calculadora.sumar(2, 3), "2 + 3 = 5"),
                () -> assertEquals(5, calculadora.sumar(3, 2), "3 + 2 = 5"),
                () -> assertEquals(0, calculadora.sumar(0, 0), "0 + 0 = 0"),
                () -> assertEquals(-1, calculadora.sumar(2, -3), "2 + (-3) = -1")
        );

    }

    @Test
    void restar() {
        assertAll("Resta",
                () -> assertEquals(-1, calculadora.restar(2, 3), "2 - 3 = -1"),
                () -> assertEquals(1, calculadora.restar(3, 2), "3 - 2 = 1"),
                () -> assertEquals(0, calculadora.restar(0, 0), "0 - 0 = 0"),
                () -> assertEquals(5, calculadora.restar(2, -3), "2 - (-3) = 5")
        );
    }

    @Test
    void multiplicar() {
        assertAll("Multiplicación",
                () -> assertEquals(6, calculadora.multiplicar(2, 3), "2 * 3 = 6"),
                () -> assertEquals(6, calculadora.multiplicar(3, 2), "3 * 2 = 6"),
                () -> assertEquals(0, calculadora.multiplicar(0, 0), "0 * 0 = 0"),
                () -> assertEquals(-6, calculadora.multiplicar(2, -3), "2 * (-3) = -6")
        );
    }

    @Test
    void dividir() {
        assertAll("División",
                () -> assertEquals(2, calculadora.dividir(6, 3), "6 / 3 = 2"),
                () -> assertEquals(0, calculadora.dividir(3, 6), "3 / 6 = 0"),
                () -> assertEquals(-2, calculadora.dividir(6, -3), "6 / (-3) = -2")
        );
    }

    @Test
    void dividirPorZeroException() {
        var ex = assertThrows(OperacionNoValidaException.class, () -> calculadora.dividir(2, 0), "No se puede dividir por cero");
        assertEquals("No se puede dividir por cero", ex.getMessage());

    }
}