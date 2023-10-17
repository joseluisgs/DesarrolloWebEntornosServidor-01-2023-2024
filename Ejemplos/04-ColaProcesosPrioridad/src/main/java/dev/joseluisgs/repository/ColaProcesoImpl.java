package dev.joseluisgs.repository;

import dev.joseluisgs.model.Proceso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * Clase ColaProcesoImpl que implementa la interfaz Cola de Procesos ordenados por prioridad.
 * Esta clase se encarga de manejar una cola de objetos de tipo Proceso.
 */
public class ColaProcesoImpl implements ColaProceso {
    /**
     * ArrayList que almacena los objetos de tipo Proceso en la cola.
     */
    private final ArrayList<Proceso> cola = new ArrayList<>();

    /**
     * Método encolar que agrega un objeto de tipo Proceso a la cola.
     * También actualiza la fecha de actualización del objeto.
     * Luego, se ordena la cola por prioridad del proceso ascendente.
     *
     * @param item Objeto de tipo Proceso a agregar a la cola.
     */
    @Override
    public void encolar(Proceso item) {
        // Actualizamos la fecha de actualización
        item.setUpdatedAt(LocalDateTime.now());
        cola.add(item);
        // Ahora debemos ordenar por prioridad del proceso ascendente
        cola.sort(Comparator.comparingInt(Proceso::getPriority));
    }

    /**
     * Método desencolar que saca el primer objeto de la cola y lo devuelve.
     * Si la cola está vacía, se devuelve una opción vacía.
     *
     * @return Opción que contiene el primer objeto de la cola o una opción vacía.
     */
    @Override
    public Optional<Proceso> desencolar() {
        // Early return
        if (cola.isEmpty()) {
            return Optional.empty();
        }
        // Sacamos el primero, lo eliminamos y lo devolvemos
        var p = cola.get(0);
        cola.remove(0);
        return Optional.of(p);
    }

    /**
     * Método frente que devuelve el primer objeto de la cola.
     * Si la cola está vacía, se devuelve una opción vacía.
     *
     * @return Opción que contiene el primer objeto de la cola o una opción vacía.
     */
    @Override
    public Optional<Proceso> frente() {
        // Early return
        if (cola.isEmpty()) {
            return Optional.empty();
        }
        // Devolvemos el primero
        return Optional.of(cola.get(0));
    }

    /**
     * Método esVacia que indica si la cola está vacía.
     *
     * @return true si la cola está vacía, false en caso contrario.
     */
    @Override
    public boolean esVacia() {
        return cola.isEmpty();
    }

    /**
     * Método size que devuelve el tamaño de la cola.
     *
     * @return Tamaño de la cola.
     */
    @Override
    public int size() {
        return cola.size();
    }

    /**
     * Método vaciar que vacía la cola.
     */
    @Override
    public void vaciar() {
        cola.clear();
    }

    /**
     * Método mostrar que muestra la cola en la consola.
     */
    @Override
    public void mostrar() {
        System.out.println("ColaProcesoImpl{" +
                "cola=" + cola +
                '}'
        );
    }
}