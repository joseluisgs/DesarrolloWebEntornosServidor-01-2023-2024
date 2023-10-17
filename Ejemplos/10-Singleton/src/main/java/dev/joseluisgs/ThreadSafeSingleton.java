package dev.joseluisgs;

public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;
    private int count;

    private ThreadSafeSingleton() {
        count = 0;
    }

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
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