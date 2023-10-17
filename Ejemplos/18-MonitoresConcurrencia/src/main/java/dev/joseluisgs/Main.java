package dev.joseluisgs;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        MiMonitorConcurrente monitor = new MiMonitorConcurrente();
        int MAX_THREADS = 50;

        // Crear 500 hilos que accedan al método sincronizado getIntegerSynchronized()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                int value = monitor.getIntegerSynchronized();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo el valor con Synchronized: " + value);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Ahora hacemos el setIntegerSynchronized()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.setIntegerSynchronized(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " estableció el valor con Synchronized: " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Crear 500 hilos que accedan al método con ReentrantLock getIntegerWithLock()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                int value = monitor.getIntegerWithLock();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo el valor ReentrantLock: " + value);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Ahora hacemos el setIntegerWithLock()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.setIntegerWithLock(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " estableció el valor con ReentrantLock: " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Crear 500 hilos que accedan al método sincronizado getListSynchronized()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                List<Integer> list = monitor.getListSynchronized();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo la lista con Synchronized: " + list);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Ahora hacemos el addToSynchronizedList()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.addToSynchronizedList(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " agregó un valor a la lista sincronizada con Synchronized: " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Crear 500 hilos que accedan al método con ReentrantLock getListWithLock()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                List<Integer> list = monitor.getListWithLock();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo la lista con ReentrantLock: " + list);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Ahora hacemos el addToLockedList()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.addToLockedList(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " agregó un valor a la lista con ReentrantLock: " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // El get de la lista concurrente no necesita sincronización
        // Crear 500 hilos que accedan al método getConcurrentList()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                List<Integer> list = monitor.getConcurrentList();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo la lista concurrente: " + list);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Crear 500 hilos que accedan al método addToConcurrentList()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.addToConcurrentList(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " agregó un valor a la lista concurrente : " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }


        // Crear 500 hilos que accedan al método setAtomicInteger()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                var valor = (int) Math.round(Math.random() * 500);
                monitor.setAtomicInteger(valor);
                System.out.println("Hilo " + Thread.currentThread().getId() + " estableció el AtomicInteger: " + valor);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }

        // Ahora hacemos el getAtomicInteger()
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread thread = new Thread(() -> {
                int value = monitor.getAtomicInteger();
                System.out.println("Hilo " + Thread.currentThread().getId() + " obtuvo el AtomicInteger: " + value);
                try {
                    Thread.sleep(Math.round(Math.random() * 500));
                } catch (InterruptedException e) {
                    System.err.println("Error en el hilo: " + Thread.currentThread().getId());
                }
            });
            thread.start();
        }


    }
}