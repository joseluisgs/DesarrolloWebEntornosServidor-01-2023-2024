package dev.joseluisgs.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Los Streams se basan en el paradigma de programación funcional.
 * Streams son secuenciales por naturaleza y se ejecutan en un único hilo,
 * lo que los hace adecuados para operaciones en un contexto de programación secuencial
 * las operaciones se ejecutan en secuencia, una después de la otra, en un hilo de ejecución único.
 * Las operaciones bloqueantes pueden detener la ejecución hasta que se complete una operación,
 * lo que puede llevar a una espera innecesaria y un uso ineficiente de los recursos y que cada operación espera
 * a que la anterior se complete antes de ejecutarse.
 */
public class DumbledoreSecuencial {
    private static final Logger logger = LoggerFactory.getLogger(DumbledoreSecuencial.class);
    private static final int MAX_SPELLS = 50;
    private final Character dubledore = Character.DUMBLEDORE;

    public Stream<Spell> emitirHechizos(Character character) {
        System.out.println(dubledore.getName() + " ha llegado para emitir hechizos secuencialmente!");
        System.out.println(dubledore.getName() + " -> Estoy lanzando hechizos a: " + character.getName() + " secuencialmente...");

        AtomicInteger total = new AtomicInteger();

        return Stream.generate(() -> {
                    try {
                        Thread.sleep(new Random().nextInt(401) + 300); // Esto es para que no sea tan rápido
                    } catch (InterruptedException e) {
                        logger.error("Error en la emisión de hechizos: " + e.getMessage());
                    }

                    total.getAndIncrement();

                    var spell = new Spell(
                            total.get() + 1,
                            "Hechizo " + (total.get() + 1),
                            Spell.Type.values()[new Random().nextInt(Spell.Type.values().length)]
                    );

                    logger.debug(dubledore.getName() + " -> He lanzado el hechizo secuencialmente " + spell.getName() + " de tipo " + spell.getType() + " a " + character.getName());
                    return spell;
                })
                .limit(MAX_SPELLS)
                .onClose(() -> System.out.println(dubledore.getName() + " -> He terminado de lanzar hechizos secuencialmente a " + character.getName()));
    }
}