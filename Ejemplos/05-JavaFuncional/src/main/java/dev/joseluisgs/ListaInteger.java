package dev.joseluisgs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListaInteger {
    private final ArrayList<Integer> lista;

    public ListaInteger() {
        this.lista = new ArrayList<Integer>();
    }

    public void add(Integer i) {
        this.lista.add(i);
    }

    public List<Integer> filtradoPorNumeroPar() {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer i : this.lista) {
            if (i % 2 == 0) {
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> filter(Function<Integer, Boolean> f) {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer i : this.lista) {
            if (f.apply(i)) {
                result.add(i);
            }
        }
        return result;
    }
}
