package com.uca.core;

import com.uca.dao.PokemonDAO;
import com.uca.dao.UserDAO;
import com.uca.entity.PokemonEntity;
import com.uca.entity.UserEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserCore {

    /* Register user in web site */

    public static UserEntity register(UserEntity obj) {
        return new UserDAO().create(obj);
    }

    /* Connect user in web site */

    public static UserEntity connection(UserEntity obj) {
        return new UserDAO().connection(obj);
    }

    /* Get a free pokemon */

    public static PokemonEntity getFreePokemon(UserEntity user) {
        PokemonEntity pokemon = new UserDAO().getFreePokemon();
        if (pokemon == null) {
            return null;
        }
        pokemon.setLevel(1);
        pokemon = new PokemonDAO().savePokemon(pokemon, user.getId());
        return pokemon;
    }

    public static UserEntity getById(Integer id) throws Exception {
        UserEntity user = new UserDAO().getById(id);
        if (user.getId() == 0) {
            throw new Exception("This user does not exists.");
        }

        user.setPassword(null);
        return user;
    }

    public static UserEntity getByUserName(String userName) throws Exception {
        UserEntity user = new UserDAO().getByUserName(userName);
        if (user.getId() == 0) {
            throw new Exception("This user does not exists.");
        }

        user.setPassword(null);
        return user;
    }

    public static void saveLastFreePokemon(UserEntity user) {
        new UserDAO().saveLastFreePokemon(user);
    }

    public static void saveLevel(Date date, Integer levelUpPerDay, Integer id) {
        new UserDAO().saveLevel(date, levelUpPerDay, id);
    }

    public static ArrayList<UserEntity> getAllUser(Integer id) {
        return new UserDAO().getAllUsers(id);
    }
}
