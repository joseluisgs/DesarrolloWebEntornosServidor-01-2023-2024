package dev.joseluisgs.calculadora;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor // Si le pongo esto no necesito el constructor porque los crea, al ponerle final!!
public class CalculadoraService {
    private final Logger logger = LoggerFactory.getLogger(CalculadoraService.class);
    private final Calculadora calculadora;

    /*
    CalculadoraService(Calculadora calculadora) {
        this.calculadora = calculadora;
    }
    */

    public int sumar(int a, int b) {
        logger.debug("Sumando " + a + " + " + b);
        return calculadora.sumar(a, b);
    }

    public int restar(int a, int b) {
        logger.debug("Restando " + a + " - " + b);
        return calculadora.restar(a, b);
    }

    public int multiplicar(int a, int b) {
        logger.debug("Multiplicando " + a + " * " + b);
        return calculadora.multiplicar(a, b);
    }

    public int dividir(int a, int b) throws OperacionNoValidaException {
        logger.debug("Dividiendo " + a + " / " + b);
        return calculadora.dividir(a, b);
    }
}