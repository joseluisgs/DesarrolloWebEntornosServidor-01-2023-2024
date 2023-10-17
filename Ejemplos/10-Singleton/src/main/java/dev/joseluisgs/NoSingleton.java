package dev.joseluisgs;

public class NoSingleton {
    private int count;

    NoSingleton() {
        count = 0;
    }

    public void increaseCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}