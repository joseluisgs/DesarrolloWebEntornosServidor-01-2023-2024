package dev.joseluisgs;

import dev.joseluisgs.models.Character;
import dev.joseluisgs.models.DumbledoreSecuencial;
import dev.joseluisgs.models.Spell;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Los Streams se basan en el paradigma de programación funcional.
 * Streams son secuenciales por naturaleza y se ejecutan en un único hilo,
 * lo que los hace adecuados para operaciones en un contexto de programación secuencial
 * las operaciones se ejecutan en secuencia, una después de la otra, en un hilo de ejecución único.
 * Las operaciones bloqueantes pueden detener la ejecución hasta que se complete una operación,
 * lo que puede llevar a una espera innecesaria y un uso ineficiente de los recursos y que cada operación espera
 * a que la anterior se complete antes de ejecutarse.
 */

public class MainSecuencial {
    public static void main(String[] args) {

        System.out.println("Harry Potter y los hechizos de la programación reactiva");

        // Creamos a DumbledoreReactive y comienza a lanzar hechizos
        var dumbledore = new DumbledoreSecuencial();

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
                    .limit(3)
                    .forEach(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.HARRY_POTTER.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a coger porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
            System.out.println(Character.HARRY_POTTER.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.ATTACK);
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
                    .limit(5)
                    .forEach(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.HERMIONE_GRANGER.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a usar porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
            System.out.println(Character.HERMIONE_GRANGER.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.HEAL);

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
                    .limit(4)
                    .forEach(
                            spell -> {
                                total.getAndIncrement();
                                System.out.println(Character.RON_WEASLEY.getName() + " -> Conozco el hechizo " + spell.getName() + " y lo voy a usar porque es de tipo " + spell.getType() + " y ya tengo " + total + " hechizos");
                                try {
                                    Thread.sleep(new Random().nextInt(500) + 200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
            System.out.println(Character.RON_WEASLEY.getName() + " -> He terminado de coger sus hechizos de " + Spell.Type.DEFENSE);
        });

        executorService.shutdown();

    }
}