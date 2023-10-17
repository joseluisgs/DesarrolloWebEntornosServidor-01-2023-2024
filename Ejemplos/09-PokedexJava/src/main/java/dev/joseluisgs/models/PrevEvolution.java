package dev.joseluisgs.models;

import lombok.Data;

@Data
public class PrevEvolution {
    public String num;
    public String name;

    @Override
    public String toString() {
        return "PrevEvolution{" + "num=" + num + ", name=" + name + '}';
    }
}