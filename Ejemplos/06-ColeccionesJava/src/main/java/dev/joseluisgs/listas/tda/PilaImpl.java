package dev.joseluisgs.listas.tda;

import java.util.ArrayList;

public class PilaImpl<T> extends ArrayList<T> implements Pila<T> {
    @Override
    public void push(T item) {
        this.add(0, item);
    }

    @Override
    public T pop() {
        return this.remove(0);
    }

    @Override
    public T peek() {
        return this.get(0);
    }

}
