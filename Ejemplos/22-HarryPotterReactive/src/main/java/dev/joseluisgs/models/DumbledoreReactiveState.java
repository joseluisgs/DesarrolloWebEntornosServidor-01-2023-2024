package dev.joseluisgs.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Random;

/**
 * ahora creamos un estado reactivo, pero del tipo Hot!, es decir toso reciban el mismo flujo de datos
 * Para eso usamos ConnectableFlux
 */
public class DumbledoreReactiveState {
    private static final Logger logger = LoggerFactory.getLogger(DumbledoreReactiveState.class);
    private static final int MAX_SPELLS = 50;

    private final Character dubledore = Character.DUMBLEDORE;
    // private final ConnectableFlux<Spell> spellState = this.emitSpellsReactively().publish();
    private final Flux<Spell> spellState = this.emitSpellsReactively().share();


    public DumbledoreReactiveState() {

    }

    public Flux<Spell> getSpellState() {
        return spellState;
    }

    public void init() {
        // this.spellState.connect();
    }

    private Flux<Spell> emitSpellsReactively() {
        System.out.println(dubledore.getName() + " ha llegado para emitir hechizos reactivamente!");
        System.out.println(dubledore.getName() + " -> Estoy lanzando hechizos reactivamente...");
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
                logger.debug(dubledore.getName() + " -> He lanzado el hechizo reactivamente " + spell.getName() + " de tipo " + spell.getType());
                sink.next(spell);
            }
            System.out.println(dubledore.getName() + " -> He terminado de lanzar hechizos reactivamente");
            sink.complete();
        });
    }
}