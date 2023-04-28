package com.uca.dao;

import com.uca.entity.PokemonEntity;
import com.uca.entity.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

public class PokemonDAO extends _Generic<PokemonEntity> {

    public PokemonEntity savePokemon(PokemonEntity pokemon, Integer userId) {
        Integer id = -1;
        pokemon.setIdPokemon(id);
        try {
            Integer compteur = 0;
            Integer idType[] = new Integer[2];
            PreparedStatement requestType;
            PreparedStatement requestId;
            PreparedStatement addPokemon;
            ResultSet result;
            String type;

            /* Verify if pokemon type is on database */

            Iterator<String> types = pokemon.getType().iterator();
            while (types.hasNext()) {
                requestType = connect.prepareStatement("Select id FROM type where type = ?");
                type = types.next();
                requestType.setString(1, type);
                result = requestType.executeQuery();
                if (!result.next()) {
                    // addType(type);
                    requestType = connect.prepareStatement("Select id FROM type where type = ?");
                    requestType.setString(1, type);
                    result = requestType.executeQuery();
                }
                result.next();
                idType[compteur] = result.getInt("id");
                compteur++;
            }

            addPokemon = connect.prepareStatement("Insert INTO pokemon VALUES ?,?,?,?,?,?");
            addPokemon.setInt(1, userId);
            addPokemon.setInt(2, pokemon.getIdAPI());
            addPokemon.setInt(3, pokemon.getLevel());
            addPokemon.setString(4, pokemon.getSprite());
            addPokemon.setInt(5, idType[0]);
            addPokemon.setInt(6, idType[1]);
            addPokemon.executeUpdate();

            requestId = connect.prepareStatement("SELECT MAX(id) FROM pokemon where idAPI=?;");
            requestId.setInt(1, pokemon.getIdAPI());
            result = requestId.executeQuery();
            result.next();
            pokemon.setIdPokemon(result.getInt("id"));

            return pokemon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PokemonEntity create(PokemonEntity obj) {
        return null;
    }

    @Override
    public boolean delete(PokemonEntity obj) {
        return false;
    }
}
