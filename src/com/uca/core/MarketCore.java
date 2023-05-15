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

    public static String exchange(MarketEntity obj, Integer idOwner, Integer idPokemon) throws SQLException {
        return new MarketDAO().exchange(obj, idOwner, idPokemon);
    }
}