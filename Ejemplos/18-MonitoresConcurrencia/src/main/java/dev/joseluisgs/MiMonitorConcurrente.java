package dev.joseluisgs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Recurso compartido
 */
public class MiMonitorConcurrente {
    // Este entero se puede leer y escribir, lo logico es protegerlo en acceso exclusivo
    private int integer = 0;

    // Este entero es una variable atómica, por lo que no necesita protección
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    // Este ArrayList se puede leer y escribir, lo logico es protegerlo en acceso exclusivo
    private final List<Integer> list = new ArrayList<>();

    // Este ArrayList es concurrente, por lo que no necesita protección
    private final List<Integer> concurrentList = new CopyOnWriteArrayList<>();
    private final Lock lockInteger = new ReentrantLock(); // Lock para proteger el acceso al entero
    private final Lock lockList = new ReentrantLock(); // Lock para proteger el acceso al ArrayList

    // Acceso al entero utilizando un método sincronizado
    public synchronized int getIntegerSynchronized() {
        return integer;
    }

    public synchronized void setIntegerSynchronized(int value) {
        integer = value;
    }

    // Acceso al entero utilizando ReentrantLock
    public int getIntegerWithLock() {
        lockInteger.lock();
        try {
            return integer;
        } finally {
            lockInteger.unlock();
        }
    }

    public void setIntegerWithLock(int value) {
        lockInteger.lock();
        try {
            integer = value;
        } finally {
            lockInteger.unlock();
        }
    }

    // Acceso al ArrayList utilizando un método sincronizado
    public synchronized List<Integer> getListSynchronized() {
        return list;
    }

    public synchronized void addToSynchronizedList(int value) {
        list.add(value);
    }

    // Acceso al ArrayList utilizando ReentrantLock
    public List<Integer> getListWithLock() {
        lockList.lock();
        try {
            return list;
        } finally {
            lockList.unlock();
        }
    }

    public void addToLockedList(int value) {
        lockList.lock();
        try {
            list.add(value);
        } finally {
            lockList.unlock();
        }
    }

    // Acceso a la colección concurrente (CopyOnWriteArrayList)
    public List<Integer> getConcurrentList() {
        return concurrentList;
    }

    public void addToConcurrentList(int value) {
        concurrentList.add(value);
    }

    // Acceso al AtomicInteger
    public int getAtomicInteger() {
        return atomicInteger.get();
    }

    public void setAtomicInteger(int value) {
        atomicInteger.set(value);
    }
}