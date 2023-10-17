package dev.joseluisgs.calculadora;

public abstract class CalculadoraExcepcion extends Exception {
    public CalculadoraExcepcion(String message) {
        super(message);
    }
}