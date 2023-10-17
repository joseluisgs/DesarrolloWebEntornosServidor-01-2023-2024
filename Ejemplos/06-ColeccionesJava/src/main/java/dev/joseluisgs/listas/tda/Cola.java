package dev.joseluisgs.listas.tda;

public interface Cola<T> {
    void enqueue(T item);

    T dequeue();

    T first();

    boolean isEmpty();

    int size();
}
