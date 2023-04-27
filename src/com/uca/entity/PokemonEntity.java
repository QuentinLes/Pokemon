package com.uca.entity;

import com.uca.core.UserCore;
import com.uca.dao.UserDAO;

public class PokemonEntity {

    private Integer idAPI;
    private Integer idPokemon;
    private Integer level;
    private String name;
    private String type;
    private String sprite;
    private Integer idPokemonEvolution;

    PokemonEntity() {

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

    public String getType() {
        return this.type;
    }

    public Integer getIdPokemonEvolution() {
        return this.idPokemonEvolution;
    }
}
