package dev.joseluisgs;

/**
 * Extender de Thread, no es recomedable si ya necesitas heredar de otra cosa
 */
public class MiClaseRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hola desde la clase que implementa Runnable " + Thread.currentThread().getName());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
        }
    }

}
