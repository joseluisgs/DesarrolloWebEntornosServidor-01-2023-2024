package dev.joseluisgs;

import dev.joseluisgs.models.Character;
import dev.joseluisgs.models.DumbledoreReactive;
import dev.joseluisgs.models.Spell;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flux es parte de la programación reactiva y está diseñado para trabajar con flujos de datos asincrónicos y eventos concurrentes.
 * Flux puede manejar múltiples hilos y permite la ejecución paralela y la concurrencia.
 * se basa en el manejo de flujos de datos asincrónicos y eventos concurrentes diseñados para reaccionar a los cambios y eventos
 * en tiempo real, en lugar de esperar pasivamente a que se complete una operación. Se basa en la propagación de eventos y
 * la notificación de cambios, lo que permite un enfoque más eficiente y escalable para manejar flujos de datos en tiempo real.
 * <p>
 * Recuerda que son ColdStream y que se ejecutan cuando se suscriben de nuevo
 */

public class MainReactive {
    public static void main(String[] args) {

        System.out.println("Harry Potter y los hechizos de la programación reactiva");

        // Creamos a DumbledoreReactive y comienza a lanzar hechizos
        var dumbledore = new DumbledoreReactive();

        // Queremos que vaya en paralelo
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(3000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Character.HARRY_POTTER.getName() + " -> Listo para coger mis hechizos de: " + Spell.Type.ATTACK);
            AtomicInteger total = new AtomicInteger();
            dumbledore.emitirHechizos(Character.HARRY_POTTER)
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
            dumbledore.emitirHechizos(Character.HERMIONE_GRANGER)
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
            dumbledore.emitirHechizos(Character.RON_WEASLEY)
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