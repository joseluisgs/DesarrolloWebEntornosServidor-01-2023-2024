package dev.joseluisgs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListaGenerica<T> {
    ArrayList<T> list;

    public ListaGenerica() {
        this.list = new ArrayList<T>();
    }

    public void add(T s) {
        this.list.add(s);
    }

    public List<T> filter(Function<T, Boolean> f) {
        List<T> result = new ArrayList<T>();
        for (T s : this.list) {
            if (f.apply(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<T> map(Function<T, T> f) {
        List<T> result = new ArrayList<T>();
        for (T s : this.list) {
            result.add(f.apply(s));
        }
        return result;
    }

    public void forEach(Function<T, Void> f) {
        for (T s : this.list) {
            f.apply(s);
        }
    }

    public T find(Function<T, Boolean> f) {
        for (T s : this.list) {
            if (f.apply(s)) {
                return s;
            }
        }
        return null;
    }

    public int findIndex(Function<T, Boolean> f) {
        int index = 0;
        for (T s : this.list) {
            if (f.apply(s)) {
                return index;
            }
            index++;
        }
        return -1;
    }

}
