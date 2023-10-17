package dev.joseluisgs.models;

import lombok.Data;

@Data
public class Spell {
    private final int id;
    private final String name;
    private final Type type;

    public enum Type {
        ATTACK,
        DEFENSE,
        HEAL,
        BUFF,
        DEBUFF,
        NONE
    }
}
