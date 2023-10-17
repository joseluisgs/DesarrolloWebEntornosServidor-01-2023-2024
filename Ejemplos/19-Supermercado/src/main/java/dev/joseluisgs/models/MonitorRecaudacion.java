package dev.joseluisgs.models;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Monitor de acceso concurrente a la recaudación
 * Podrias hacerlo con un AtomicInteger?? Qué problemas habría?
 */
public class MonitorRecaudacion {
    private final Lock lockRecaudacion = new ReentrantLock(true); // Para que sea justo
    private int recaudacion = 0;

    public void addRecaudacion(int recaudacion) {
        lockRecaudacion.lock();
        try {
            this.recaudacion += recaudacion;
        } finally {
            lockRecaudacion.unlock();
        }
    }

    // Esto esta hecho muy bestia pero es para que veas que se puede hacer
    public int getRecaudacion() {
        var recaudacion = 0;
        lockRecaudacion.lock();
        try {
            recaudacion = this.recaudacion;
        } finally {
            lockRecaudacion.unlock();
        }
        return recaudacion;
    }
}
