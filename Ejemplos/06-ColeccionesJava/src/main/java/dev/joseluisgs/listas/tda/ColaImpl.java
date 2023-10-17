package dev.joseluisgs.listas.tda;

import java.util.ArrayList;

public class ColaImpl<T> extends ArrayList<T> implements Cola<T> {

    @Override
    public void enqueue(T item) {
        this.add(item);
    }

    @Override
    public T dequeue() {
        return this.remove(0);
    }

    @Override
    public T first() {
        return this.get(0);
    }
}
