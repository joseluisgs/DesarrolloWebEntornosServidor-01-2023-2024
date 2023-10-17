package dev.joseluisgs;

import dev.joseluisgs.models.Character;
import dev.joseluisgs.models.DumbledoreReactiveState;
import dev.joseluisgs.models.Spell;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ahora trabajamos con un estado reactivo que puede ir cambiando y todos los comparten.
 * Ya no es un flujo de datos frío, sino caliente.
 */

public class MainReactiveState {
    public static void main(String[] args) {

        System.out.println("Harry Potter y los hechizos de la programación reactiva");

        // Creamos a DumbledoreReactive y comienza a lanzar hechizos
        var dumbledore = new DumbledoreReactiveState();

        // Queremos que vaya en paralelo
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Lo ejecutamos a dulmbledore
        executorService.submit(dumbledore::init);

        executorService.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(3000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Character.HARRY_POTTER.getName() + " -> Listo para coger mis hechizos de: " + Spell.Type.ATTACK);
            AtomicInteger total = new AtomicInteger();
            dumbledore.getSpellState()
                    .filter(spell -> spell.getType().equals(Spell.Type.ATTACK))
                    .take(3)
                    .subscribe(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.HARRY_POTTER.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a coger porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {
                            },
                            () -> System.out.println(Character.HARRY_POTTER.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.ATTACK)
                    );
        });

        executorService.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(200) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Character.HERMIONE_GRANGER.getName() + " -> Lista para coger mis hechizos de: " + Spell.Type.HEAL);
            AtomicInteger total = new AtomicInteger();
            dumbledore.getSpellState()
                    .filter(spell -> spell.getType().equals(Spell.Type.HEAL))
                    .take(5)
                    .subscribe(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.HERMIONE_GRANGER.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a usar porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {
                            },
                            () -> System.out.println(Character.HERMIONE_GRANGER.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.HEAL)
                    );
        });

        executorService.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(200) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Character.RON_WEASLEY.getName() + " -> Lista para coger mis hechizos de: " + Spell.Type.DEFENSE);
            AtomicInteger total = new AtomicInteger();
            dumbledore.getSpellState()
                    .filter(spell -> spell.getType().equals(Spell.Type.DEFENSE))
                    .take(4)
                    .subscribe(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.RON_WEASLEY.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a usar porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {
                            },
                            () -> System.out.println(Character.RON_WEASLEY.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.DEFENSE)
                    );
        });

        executorService.shutdown();

    }
}