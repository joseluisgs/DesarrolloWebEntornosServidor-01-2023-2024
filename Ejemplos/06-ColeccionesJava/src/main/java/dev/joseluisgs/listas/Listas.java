package dev.joseluisgs.listas;

import dev.joseluisgs.listas.tda.ColaImpl;
import dev.joseluisgs.listas.tda.PilaImpl;

import java.util.ArrayList;

public class Listas {
    public static void main(String[] args) {
        var lista1 = new ArrayList<Integer>();
        lista1.add(1);
        lista1.add(2);
        lista1.add(3);
        lista1.add(4);
        lista1.remove(2);

        // vamos a recorrerlo
        for (Integer i : lista1) {
            System.out.println(i);
        }

        System.out.println();

        // Pila
        var pila1 = new PilaImpl<Integer>();
        pila1.push(1);
        pila1.push(2);
        pila1.push(3);
        pila1.push(4);

        // vamos a recorrerlo
        for (Integer i : pila1) {
            System.out.println(i);
        }

        System.out.println();

        pila1.pop();
        pila1.pop();

        // vamos a recorrerlo
        for (Integer i : pila1) {
            System.out.println(i);
        }

        System.out.println();

        var cola1 = new ColaImpl<Integer>();
        cola1.enqueue(1);
        cola1.enqueue(2);
        cola1.enqueue(3);

        // vamos a recorrerlo
        for (Integer i : cola1) {
            System.out.println(i);
        }
        System.out.println();

        cola1.dequeue();
        cola1.dequeue();

        // vamos a recorrerlo
        for (Integer i : cola1) {
            System.out.println(i);
        }

        System.out.println();

        lista1.sort((a, b) -> a - b);

    }
}
