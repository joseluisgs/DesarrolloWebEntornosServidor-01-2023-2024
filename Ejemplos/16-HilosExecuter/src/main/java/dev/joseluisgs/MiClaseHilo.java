package dev.joseluisgs;

/**
 * Extender de Thread, no es recomedable si ya necesitas heredar de otra cosa
 */
public class MiClaseHilo extends Thread {
    @Override
    public void run() {
        System.out.println("Hola desde la clase que hereda de Thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.err.println("Error en el hilo " + Thread.currentThread().getName() + ": " + ex.getMessage());
        }
    }

}
