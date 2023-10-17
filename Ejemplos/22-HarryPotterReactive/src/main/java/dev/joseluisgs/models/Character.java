package dev.joseluisgs.models;

public class Character {
    public static final Character HARRY_POTTER = new Character("Harry Potter");
    public static final Character HERMIONE_GRANGER = new Character("Hermione Granger");
    public static final Character RON_WEASLEY = new Character("Ron Weasley");
    public static final Character DUMBLEDORE = new Character("Dumbledore");

    private final String name;

    private Character(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
