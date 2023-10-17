package dev.joseluisgs.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Random;

/**
 * Flux es parte de la programación reactiva y está diseñado para trabajar con flujos de datos asincrónicos y eventos concurrentes.
 * Flux puede manejar múltiples hilos y permite la ejecución paralela y la concurrencia.
 * se basa en el manejo de flujos de datos asincrónicos y eventos concurrentes diseñados para reaccionar a los cambios y eventos
 * en tiempo real, en lugar de esperar pasivamente a que se complete una operación. Se basa en la propagación de eventos y
 * la notificación de cambios, lo que permite un enfoque más eficiente y escalable para manejar flujos de datos en tiempo real.
 */
public class DumbledoreReactive {
    private static final Logger logger = LoggerFactory.getLogger(DumbledoreReactive.class);
    private static final int MAX_SPELLS = 50;

    private final Character dubledore = Character.DUMBLEDORE;

    public Flux<Spell> emitirHechizos(Character character) {
        System.out.println(dubledore.getName() + " ha llegado para emitir hechizos reactivamente!");
        System.out.println(dubledore.getName() + " -> Estoy lanzando hechizos a: " + character.getName() + " reactivamente...");
        return Flux.create(sink -> {
            for (int i = 0; i < MAX_SPELLS; i++) {
                try {
                    Thread.sleep(new Random().nextInt(401) + 300); // Esto es para que no sea tan rápido
                } catch (InterruptedException e) {
                    logger.error("Error en la emisión de hechizos: " + e.getMessage());
                }
                var spell = new Spell(
                        i + 1,
                        "Hechizo " + (i + 1),
                        Spell.Type.values()[new Random().nextInt(Spell.Type.values().length)]
                );
                logger.debug(dubledore.getName() + " -> He lanzado el hechizos reactivamente " + spell.getName() + " de tipo " + spell.getType() + " a " + character.getName());
                sink.next(spell);
            }
            System.out.println(dubledore.getName() + " -> He terminado de lanzar hechizos reactivamente a " + character.getName());
            sink.complete();
        });
    }
}