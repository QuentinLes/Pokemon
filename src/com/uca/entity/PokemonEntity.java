package com.uca.entity;

import com.uca.core.UserCore;
import com.uca.dao.UserDAO;
import java.util.ArrayList;

public class PokemonEntity {

    private Integer idAPI;
    private Integer idPokemon;
    private Integer level;
    private String name;
    private ArrayList<String> type;
    private String sprite;

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

}
