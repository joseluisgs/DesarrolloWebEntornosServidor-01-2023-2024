package dev.joseluisgs.models;


import lombok.Data;

import java.util.ArrayList;

@Data
public class Pokemon {
    public int id;
    public String num;
    public String name;
    public String img;
    public ArrayList<String> type;
    public String height;
    public String weight;
    public String candy;
    public int candy_count;
    public String egg;
    public double spawn_chance;
    public double avg_spawns;
    public String spawn_time;
    public ArrayList<Double> multipliers;
    public ArrayList<String> weaknesses;
    public ArrayList<NextEvolution> next_evolution;
    public ArrayList<PrevEvolution> prev_evolution;

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", type=" + type +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", candy='" + candy + '\'' +
                ", candy_count=" + candy_count +
                ", egg='" + egg + '\'' +
                ", spawn_chance=" + spawn_chance +
                ", avg_spawns=" + avg_spawns +
                ", spawn_time='" + spawn_time + '\'' +
                ", multipliers=" + multipliers +
                ", weaknesses=" + weaknesses +
                ", next_evolution=" + next_evolution +
                ", prev_evolution=" + prev_evolution +
                '}';
    }
}

