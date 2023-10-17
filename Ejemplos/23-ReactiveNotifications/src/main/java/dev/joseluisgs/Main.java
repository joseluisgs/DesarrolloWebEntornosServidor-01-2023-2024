package dev.joseluisgs;

import dev.joseluisgs.models.Funko;
import dev.joseluisgs.repository.FunkoRepositoryConnecatbleFlux;

import java.util.UUID;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // FunkoRepositoryFluxSink repository = new FunkoRepositoryFluxSink();
        FunkoRepositoryConnecatbleFlux repository = new FunkoRepositoryConnecatbleFlux();

        System.out.println("Systema de obtención de la lista en Tiempo Real");
        repository.getAllAsFlux().subscribe(
                lista -> {
                    System.out.println("👉 Lista de Funkos actulizada: " + lista);
                },
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );

        System.out.println("Sistema de obtención de notificaciones en Tiempo Real");
        repository.getNotificationAsFlux().subscribe(
                notificacion -> System.out.println("🟢 Notificación: " + notificacion),
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );

        Funko funko1 = new Funko(UUID.randomUUID(), "Funko1", 10.0);
        System.out.println("Añadimos un nuevo Funko: " + funko1);
        repository.add(funko1);
        Thread.sleep(5000);

        Funko funko2 = new Funko(UUID.randomUUID(), "Funko2", 20.0);
        System.out.println("Añadimos un nuevo Funko: " + funko2);
        repository.add(funko2);
        Thread.sleep(5000);

        System.out.println("Eliminamos un Funko: " + funko1);
        repository.delete(funko1.getId());
        Thread.sleep(5000);

        Funko funko3 = new Funko(UUID.randomUUID(), "Funko3", 30.0);
        System.out.println("Añadimos un nuevo Funko: " + funko3);
        repository.add(funko3);
        Thread.sleep(5000);

        System.out.println("Eliminamos un Funko: " + funko2);
        repository.delete(funko2.getId());
        Thread.sleep(5000);
    }
}