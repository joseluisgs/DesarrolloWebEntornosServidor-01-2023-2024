package dev.joseluisgs;

public class Main {

    public static void main(String[] args) {
        System.out.println("Accidentes Madrid");
        System.out.println("==================");
        AccidentesController controller = new AccidentesController();
        controller.run();

    }
}
