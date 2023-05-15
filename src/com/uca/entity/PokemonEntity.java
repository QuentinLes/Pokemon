package com.uca.entity;

import com.uca.core.PokemonCore;

import java.util.ArrayList;

public class PokemonEntity {

    private Integer idAPI;
    private Integer idPokemon;
    private Integer level;
    private String name;
    private ArrayList<String> type;
    private String sprite;

    private Integer shiny;

    public PokemonEntity() {
        this.type = new ArrayList<String>();
    }

    /* GETTEUR */

    public Integer getIdAPI() {
        return this.idAPI;
    }

    public Integer getIdPokemon() {
        return this.idPokemon;
    }

    public Integer getLevel() {
        return this.level;
    }

    public String getName() {
        return this.name;
    }

    public String getSprite() {
        return this.sprite;
    }

    public ArrayList<String> getType() {
        return this.type;
    }

    public Integer getShiny() {
        return this.shiny;
    }


    /* SETTEUR */

    public void setIdAPI(Integer idAPI) {
        this.idAPI = idAPI;
    }

    public void setIdPokemon(Integer idPokemon) {
        this.idPokemon = idPokemon;
    }

    public void setLevel(Integer level) {
        if (level <= 100) {
            this.level = level;
        } else {
            this.level = 100;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public void setType(String type) {
        this.type.add(type);
    }

    public void setShiny(Integer shiny) {
        this.shiny = shiny;
    }

    /* METHOD */

    public boolean levelUp() {
        if (level < 100) {
            level++;

            PokemonCore.saveLevel(level, idPokemon);
            return true;
        }
        return false;
    }
}
