package dev.joseluisgs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListaString {
    ArrayList<String> list;

    public ListaString() {
        this.list = new ArrayList<String>();
    }

    public void add(String s) {
        this.list.add(s);
    }

    public List<String> filter(Function<String, Boolean> f) {
        List<String> result = new ArrayList<String>();
        for (String s : this.list) {
            if (f.apply(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<String> map(Function<String, String> f) {
        List<String> result = new ArrayList<String>();
        for (String s : this.list) {
            result.add(f.apply(s));
        }
        return result;
    }

    public void forEach(Function<String, Void> f) {
        for (String s : this.list) {
            f.apply(s);
        }
    }

    public String find(Function<String, Boolean> f) {
        for (String s : this.list) {
            if (f.apply(s)) {
                return s;
            }
        }
        return null;
    }

    public int findIndex(Function<String, Boolean> f) {
        int index = 0;
        for (String s : this.list) {
            if (f.apply(s)) {
                return index;
            }
            index++;
        }
        return -1;
    }

}
