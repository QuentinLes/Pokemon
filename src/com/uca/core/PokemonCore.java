package com.uca.core;

import com.uca.dao.PokemonDAO;
import com.uca.dao.UserDAO;
import com.uca.entity.UserEntity;
import com.uca.entity.PokemonEntity;

import java.util.ArrayList;

public class PokemonCore {

    public static ArrayList<PokemonEntity> getAllPokemon(Integer idProprietaire) throws Exception {
        ArrayList<PokemonEntity> pokemon = new PokemonDAO().getAllPokemon(idProprietaire);
        return pokemon;
    }

    public static ArrayList<String> getAllName() {
        return new PokemonDAO().getAllName();
    }

    public static void saveLevel(Integer level, Integer id) {
        new PokemonDAO().saveLevel(level, id);
    }

    public static PokemonEntity getPokemonById(Integer idPokemon) {
        return new PokemonDAO().getPokemonById(idPokemon);
    }

    public static PokemonEntity getPokemonByIdAPI(Integer idPokemonAPI, Integer shiny) {
        if (idPokemonAPI > 0 && idPokemonAPI < 1009) {
            return new PokemonDAO().getPokemonByIdAPI(idPokemonAPI, shiny);
        }
        return null;
    }

    public static void addAllPokemon() {
        boolean verification = new PokemonDAO().addAllPokemonInPokedex();
        if (!verification) {
            System.out.println("Probeme lors de l'insertion de tout les pokemons");
        }

    }
}
