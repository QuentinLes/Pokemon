package com.uca.entity;

import com.uca.core.MarketCore;

public class MarketEntity {

    private Integer id;

    private Integer idOwner;

    private PokemonEntity wantedPokemon;

    private PokemonEntity exchangedPokemon;

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

    /* METHOD */

    public String createExchange(Integer idOwner, PokemonEntity exchangedPokemon, PokemonEntity wantedPokemon) {
        this.exchangedPokemon = exchangedPokemon;
        this.wantedPokemon = wantedPokemon;
        this.idOwner = idOwner;
        this.id = -1;
        return MarketCore.addExchange(this);
    }
}
