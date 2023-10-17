package dev.joseluisgs.services.cache;

public interface Cache<K, V> {
    void put(K key, V value);

    V get(K key);

    void remove(K key);

    void clear();

    void shutdown();
}