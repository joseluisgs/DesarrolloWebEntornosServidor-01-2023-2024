package dev.joseluisgs.utils;

/**
 * Clase de utilidad para hacer operaciones matemáticas
 *
 * @author JLGS
 * @version 1.0
 * @since 1.0
 */
public class Calculadora {
    /**
     * Suma dos números
     *
     * @param a Primer número
     * @param b Segundo número
     * @return Suma de los dos números
     */
    public int suma(int a, int b) {
        return a + b;
    }

    /**
     * Resta dos números
     *
     * @param a Primer número
     * @param b Segundo número
     * @return Resta de los dos números
     */
    public int resta(int a, int b) {
        return a - b;
    }

    /**
     * Multiplica dos números
     *
     * @param a Primer número
     * @param b Segundo número
     * @return Multiplicación de los dos números
     */
    public int multiplicacion(int a, int b) {
        return a * b;
    }

    /**
     * Divide dos números
     *
     * @param a Primer número
     * @param b Segundo número
     * @return División de los dos números
     * @throws ArithmeticException Si el divisor es cero
     */
    public int division(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("No se puede dividir por cero");
        }
        return a / b;
    }
}
