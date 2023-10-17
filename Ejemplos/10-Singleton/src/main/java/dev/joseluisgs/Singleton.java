package dev.joseluisgs;

public class Singleton {
    private static Singleton instance;
    private int count;

    private Singleton() {
        count = 0;
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void increaseCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}