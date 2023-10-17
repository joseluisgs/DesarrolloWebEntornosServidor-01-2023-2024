package dev.joseluisgs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.joseluisgs.models.Pokemon;
import dev.joseluisgs.controllers.PokemonController;

/**
 * https://json2csharp.com/code-converters/json-to-pojo
 * https://www.jsonschema2pojo.org/
 * https://plugins.jetbrains.com/plugin/8634-robopojogenerator
 * https://marketplace.visualstudio.com/items?itemName=quicktype.quicktype
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Pokedex!");
        var pokeController = PokemonController.getInstance();
        var pokemon = pokeController.getPokemon(24);
        printPokemon(pokemon);
        printPokemonJson(pokemon);
    }

    private static void printPokemon(Pokemon pokemon) {
        System.out.println(pokemon);
    }

    private static void printPokemonJson(Pokemon pokemon) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(pokemon));
    }
}
