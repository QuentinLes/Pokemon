package com.uca.dao;

import com.uca.core.PokemonCore;
import com.uca.entity.MarketEntity;

import java.sql.*;
import java.util.ArrayList;

public class MarketDAO extends _Generic<MarketEntity> {

    @Override
    public MarketEntity create(MarketEntity obj) {
        try {

            PreparedStatement request = this.connect.prepareStatement(
                    "INSERT INTO market (idOwner) VALUES (?);", Statement.RETURN_GENERATED_KEYS
            );
            System.out.println("test");
            request.setInt(1, obj.getIdOwner());
            System.out.println(("test2"));
            request.executeUpdate();
            ResultSet result = request.getGeneratedKeys();
            if (result.next()) {

                obj.setId(result.getInt(1));
                System.out.println((obj.getId()));

            } else {
                return null;
            }
            request = this.connect.prepareStatement(
                    "INSERT INTO  exchange (idExchange, idPokemonWanted, idPokemonTrade,shiny) VALUES (?,?,?,?);"
            );
            request.setInt(1, obj.getId());
            request.setInt(3, obj.getExchangedPokemon().getIdPokemon());
            request.setInt(2, obj.getWantedPokemon().getIdAPI());
            request.setInt(4, obj.getShiny());
            request.executeUpdate();
            return obj;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }
    }

    @Override
    public boolean delete(MarketEntity obj) {
        try {

            PreparedStatement request = this.connect.prepareStatement(
                    "DELETE FROM exchange where idExchange = ?;"
            );
            request.setInt(1, obj.getId());
            request.executeUpdate();
            request = this.connect.prepareStatement(
                    "DELETE FROM market where id = ?;"
            );
            request.setInt(1, obj.getId());
            request.executeUpdate();
            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }
    }

    public ArrayList<MarketEntity> getAllExchange() {
        try {

            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT id,idOwner FROM market;"
            );
            ResultSet results = request.executeQuery();
            ArrayList<MarketEntity> list = new ArrayList<>();

            while (results.next()) {

                MarketEntity newExchange = new MarketEntity();
                newExchange.setId(results.getInt("id"));
                newExchange.setIdOwner(results.getInt("idOwner"));
                PreparedStatement request2 = this.connect.prepareStatement(
                        "SELECT idExchange,idPokemonWanted,idPokemonTrade,shiny FROM exchange WHERE idExchange = ?;"
                );
                request2.setInt(1, newExchange.getId());
                ResultSet exchange = request2.executeQuery();

                while (exchange.next()) {

                    newExchange.setShiny(exchange.getInt("shiny"));
                    newExchange.setExchangedPokemon(PokemonCore.getPokemonById(exchange.getInt("idPokemonTrade")));
                    newExchange.setWantedPokemon(PokemonCore.getPokemonByIdAPI(exchange.getInt("idPokemonWanted"), 0));
                    list.add(newExchange);

                }
            }

            return list;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }
    }

    public String exchange(Integer idOwner1, Integer idPokemon1, Integer idOwner2, Integer idPokemon2, Integer idExchange) throws SQLException {
        try {
            PreparedStatement request;
            connect.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            request = this.connect.prepareStatement(
                    "UPDATE pokemon SET idOwner = ? where idPokemon = ?;"
            );
            request.setInt(1, idOwner1);
            request.setInt(2, idPokemon2);
            request.executeUpdate();

            request = this.connect.prepareStatement(
                    "UPDATE pokemon SET idOwner = ? where idPokemon = ?;"
            );
            request.setInt(1, idOwner2);
            request.setInt(2, idPokemon1);
            request.executeUpdate();

            MarketEntity obj = getExchangeById(idExchange);

            if (!delete(obj)) {
                connect.rollback();
                return "Exchange fail";
            }

            connect.commit();
            return null;

        } catch (SQLException e) {
            connect.rollback();
            return e.toString();
        }
    }

    public MarketEntity getExchangeById(Integer id) {
        MarketEntity exchange = new MarketEntity();
        try {

            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT idOwner,idPokemonWanted,idPokemonTrade,shiny FROM exchange INNER JOIN market ON exchange.idExchange = market.id WHERE idExchange = ?;"
            );
            request.setInt(1, id);
            ResultSet resultSet = request.executeQuery();
            if (resultSet.next()) {
                exchange.setId(id);
                exchange.setShiny(resultSet.getInt("shiny"));
                exchange.setIdOwner(resultSet.getInt("idOwner"));
                exchange.setExchangedPokemon(PokemonCore.getPokemonById(resultSet.getInt("idPokemonTrade")));
                exchange.setWantedPokemon(PokemonCore.getPokemonByIdAPI(resultSet.getInt("idPokemonWanted"), exchange.getShiny()));
                return exchange;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
