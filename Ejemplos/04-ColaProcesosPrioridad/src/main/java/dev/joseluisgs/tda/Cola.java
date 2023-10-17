package dev.joseluisgs.tda;

import java.util.Optional;

/**
 * Interfaz Cola que define los métodos que debe implementar cualquier clase que quiera utilizarla.
 * Esta interfaz define los métodos para agregar elementos a la cola, sacarlos, ver el primero de la cola,
 * comprobar si la cola está vacía, obtener su tamaño, vaciarla y mostrarla.
 *
 * @param <T> Tipo de los elementos que se van a almacenar en la cola.
 */
public interface Cola<T> {
    /**
     * Método encolar que agrega un elemento a la cola.
     *
     * @param item Elemento a agregar a la cola.
     */
    void encolar(T item);

    /**
     * Método desencolar que saca el primer elemento de la cola y lo devuelve.
     * Si la cola está vacía, se devuelve una opción vacía.
     *
     * @return Opción que contiene el primer elemento de la cola o una opción vacía.
     */
    Optional<T> desencolar();

    /**
     * Método frente que devuelve el primer elemento de la cola.
     * Si la cola está vacía, se devuelve una opción vacía.
     *
     * @return Opción que contiene el primer elemento de la cola o una opción vacía.
     */
    Optional<T> frente();

    /**
     * Método esVacia que indica si la cola está vacía.
     *
     * @return true si la cola está vacía, false en caso contrario.
     */
    boolean esVacia();

    /**
     * Método size que devuelve el tamaño de la cola.
     *
     * @return Tamaño de la cola.
     */
    int size();

    /**
     * Método vaciar que vacía la cola.
     */
    void vaciar();

    /**
     * Método mostrar que muestra la cola en la consola.
     */
    void mostrar();
}