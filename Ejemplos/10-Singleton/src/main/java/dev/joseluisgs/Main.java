package dev.joseluisgs;

public class Main {
    public static void main(String[] args) {
        System.out.println("No Singleton:");
        NoSingleton noSingleton1 = new NoSingleton();
        noSingleton1.increaseCount();
        System.out.println(noSingleton1.getCount());  // Debería imprimir "1"

        NoSingleton noSingleton2 = new NoSingleton();
        noSingleton2.increaseCount();
        System.out.println(noSingleton2.getCount());  // Debería imprimir "1" porque son instancias diferentes

        System.out.println("Singleton:");
        Singleton singleton1 = Singleton.getInstance();
        singleton1.increaseCount();
        System.out.println(singleton1.getCount());  // Debería imprimir "1"

        Singleton singleton2 = Singleton.getInstance();
        singleton2.increaseCount();
        System.out.println(singleton2.getCount());  // Debería imprimir "2" porque es la misma instancia

        // Si singleton1 y singleton2 son la misma instancia, entonces sus conteos deberían ser iguales.
        System.out.println(singleton1.getCount() == singleton2.getCount());  // Debería imprimir "true"
    }
}