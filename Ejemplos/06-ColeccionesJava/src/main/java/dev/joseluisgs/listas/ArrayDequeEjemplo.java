package dev.joseluisgs.listas;


import java.util.ArrayDeque;

public class ArrayDequeEjemplo {
    public static void main(String[] args) {
        var queue = new ArrayDeque<Integer>();

        // comportamiento de pila
        queue.addFirst(1);
        queue.addFirst(2);
        queue.addFirst(3);

        // recorremos la pila
        for (int n : queue) {
            System.out.println(n);
        }

        System.out.println();

        queue.removeFirst();
        queue.removeFirst();

        // recorremos la pila
        for (int n : queue) {
            System.out.println(n);
        }

        System.out.println();

        // como cola
        queue = new ArrayDeque<Integer>();
        queue.addLast(1);
        queue.addLast(2);
        queue.addLast(3);

        // recorremos la cola
        for (int n : queue) {
            System.out.println(n);
        }

        System.out.println();

        queue.removeFirst();
        queue.removeFirst();

        // recorremos la cola
        for (int n : queue) {
            System.out.println(n);
        }

    }
}
