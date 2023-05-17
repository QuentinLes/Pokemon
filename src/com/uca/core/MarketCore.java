package com.uca.core;

import com.uca.dao.MarketDAO;
import com.uca.entity.MarketEntity;

import java.sql.SQLException;
import java.util.ArrayList;


public class MarketCore {

    public static String addExchange(MarketEntity exchange) {
        new MarketDAO().create(exchange);
        if (exchange.getId() < 0) {
            return "Insertion problem";
        }
        return null;
    }

    public static ArrayList<MarketEntity> getAllExchange() {
        return new MarketDAO().getAllExchange();
    }

    public static String exchange(Integer idOwner1, Integer idPokemon1, Integer idOwner2, Integer idPokemon2, Integer idExchange) throws SQLException {
        return new MarketDAO().exchange(idOwner1, idPokemon1, idOwner2, idPokemon2, idExchange);
    }

    public static MarketEntity getExchangeById(Integer id) {
        return new MarketDAO().getExchangeById(id);
    }
}