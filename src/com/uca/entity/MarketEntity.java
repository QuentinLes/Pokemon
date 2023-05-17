package com.uca.entity;

import com.uca.core.MarketCore;

public class MarketEntity {

    private Integer id;

    private Integer idOwner;

    private PokemonEntity wantedPokemon;

    private PokemonEntity exchangedPokemon;

    private Integer shiny;

    public MarketEntity() {
        //nothing todo
    }

    /* SETTEUR */

    public void setId(Integer id) {
        this.id = id;
    }

    public void setExchangedPokemon(PokemonEntity exchangedPokemon) {
        this.exchangedPokemon = exchangedPokemon;
    }

    public void setIdOwner(Integer idOwner) {
        this.idOwner = idOwner;
    }

    public void setWantedPokemon(PokemonEntity wantedPokemon) {
        this.wantedPokemon = wantedPokemon;
    }

    public void setShiny(Integer shiny) {
        this.shiny = shiny;
    }

    /* GETTEUR */

    public Integer getId() {
        return id;
    }

    public Integer getIdOwner() {
        return idOwner;
    }

    public PokemonEntity getExchangedPokemon() {
        return exchangedPokemon;
    }

    public PokemonEntity getWantedPokemon() {
        return wantedPokemon;
    }

    public Integer getShiny() {
        return shiny;
    }

    /* METHOD */

    public String createExchange(Integer idOwner, PokemonEntity exchangedPokemon, PokemonEntity wantedPokemon, Integer shiny) {
        this.exchangedPokemon = exchangedPokemon;
        this.wantedPokemon = wantedPokemon;
        this.idOwner = idOwner;
        this.id = -1;
        this.shiny = shiny;
        return MarketCore.addExchange(this);
    }
}
