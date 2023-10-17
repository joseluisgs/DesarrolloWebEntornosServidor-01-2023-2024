package dev.joseluisgs.calculadora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calculadora {
    private final Logger logger = LoggerFactory.getLogger(Calculadora.class);

    public int sumar(int a, int b) {
        logger.debug("Sumando " + a + " + " + b);
        return a + b;
    }

    public int restar(int a, int b) {
        logger.debug("Restando " + a + " - " + b);
        return a - b;
    }

    public int multiplicar(int a, int b) {
        logger.debug("Multiplicando " + a + " * " + b);
        return a * b;
    }

    public int dividir(int a, int b) throws OperacionNoValidaException {
        logger.debug("Dividiendo " + a + " / " + b);
        if (b == 0) {
            logger.error("No se puede dividir por cero");
            throw new OperacionNoValidaException("No se puede dividir por cero");
        }
        return a / b;
    }
}