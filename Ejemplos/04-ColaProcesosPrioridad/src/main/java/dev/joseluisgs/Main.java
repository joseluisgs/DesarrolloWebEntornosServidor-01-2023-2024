package dev.joseluisgs;

import dev.joseluisgs.model.Proceso;
import dev.joseluisgs.repository.ColaProcesoImpl;

public class Main {
    public static void main(String[] args) {
        int MAX_PROCESOS = 100;
        int MAX_DURATION = 100;
        int MIN_PRIORITY = 1;
        int MAX_PRIORITY = 8;
        int QUANTUM = 5;

        System.out.println("Hola Round Robin");
        var cola = new ColaProcesoImpl();
        /*
        Proceso P1 = new Proceso("P1", 10, 1);
        Proceso P2 = new Proceso("P2", 5, 2);
        Proceso P3 = new Proceso("P3", 2, 3);

        System.out.println(P1);
        System.out.println(P2);
        System.out.println(P3);

        cola.encolar(P1);
        cola.encolar(P2);
        cola.encolar(P3);

        cola.mostrar();

        cola.vaciar();
        */

        // Creamos una Cola de Procesos de 100 elementos
        // Prioridad de 1 a 8
        // Duración de 1 a 100

        for (int i = 0; i < MAX_PROCESOS; i++) {
            cola.encolar(new Proceso("P" + i, (int) (Math.random() * MAX_DURATION) + 1, (int) (Math.random() * MAX_PRIORITY) + MIN_PRIORITY));
        }

        // Mostramos la cola
        cola.mostrar();

        // Round Robyn, mientras no este vacia, sacamos y encolamos
        System.out.println("Round Robin");
        while (!cola.esVacia()) {
            var p = cola.desencolar();
            if (p.isPresent()) {
                System.out.println(p.get());
                // Actualizamos la duración
                p.get().setDuration(p.get().getDuration() - QUANTUM);
                // Si la duración es mayor que 0, lo volvemos a encolar
                if (p.get().getDuration() > 0) {
                    cola.encolar(p.get());
                }
                System.out.println(cola.size());
            }
        }
    }


}