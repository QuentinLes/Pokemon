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
                    "INSERT INTO market (idProprietaire,idPokemonEchanger,idPokemonVoulut) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS
            );
            request.setInt(1, obj.getIdOwner());
            request.setInt(2, obj.getExchangedPokemon().getIdPokemon());
            request.setInt(3, obj.getWantedPokemon().getIdAPI());
            request.executeUpdate();
            ResultSet result = request.getGeneratedKeys();
            result.next();
            obj.setId(result.getInt(1));
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
                    "SELECT id,idOwner,idPokemonEchanger,idPokemonVoulut FROM market;"
            );
            ResultSet results = request.executeQuery();
            ArrayList<MarketEntity> list = new ArrayList<>();

            while (results.next()) {

                MarketEntity newExchange = new MarketEntity();
                newExchange.setId(results.getInt("id"));
                newExchange.setIdOwner(results.getInt("idOwner"));
                newExchange.setExchangedPokemon(PokemonCore.getPokemonById(results.getInt("idPokemonEchanger")));
                newExchange.setExchangedPokemon(PokemonCore.getPokemonByIdAPI(results.getInt("idPokemonVoulut"), 0));
                list.add(newExchange);

            }

            return list;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }
    }

    public String exchange(MarketEntity obj, Integer idOwner, Integer idPokemon) throws SQLException {
        try {
            PreparedStatement request;
            connect.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            request = this.connect.prepareStatement("SELECT * FROM pokemon WHERE idProprietaire = ?, idAPI = ?, idPokemon = ?;");
            request.setInt(1, idOwner);
            request.setInt(2, obj.getWantedPokemon().getIdAPI());
            request.setInt(3, idPokemon);
            ResultSet result = request.executeQuery();

            if (result.next()) {

                request = this.connect.prepareStatement(
                        "UPDATE pokemon SET idProprietaire = ? where idPokemon = ?;"
                );
                request.setInt(1, idOwner);
                request.setInt(2, obj.getExchangedPokemon().getIdPokemon());
                request.executeUpdate();

                request = this.connect.prepareStatement(
                        "UPDATE pokemon SET idProprietaire = ? where idPokemon = ?;"
                );
                request.setInt(1, obj.getIdOwner());
                request.setInt(2, idPokemon);
                request.executeUpdate();

                if (!delete(obj)) {
                    connect.rollback();
                    return "Exchange fail";
                }

                connect.commit();
                return null;
            }
            connect.rollback();
            return "Exchange not valid";

        } catch (SQLException e) {
            connect.rollback();
            return e.toString();
        }
    }
}
