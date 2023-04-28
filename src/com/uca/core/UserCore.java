package com.uca.core;

import com.uca.dao.PokemonDAO;
import com.uca.dao.UserDAO;
import com.uca.entity.UserEntity;
import com.uca.entity.PokemonEntity;

import java.util.ArrayList;

public class UserCore {

    public static void test() {
        new UserDAO().test();
    }

    /* Register user in web site */

    public static UserEntity register(UserEntity obj) {
        return new UserDAO().create(obj);
    }

    /* Connect user in web site */

    public static UserEntity connection(UserEntity obj) {
        return new UserDAO().connection(obj);
    }

    /* Get a free pokemon */

    public static PokemonEntity getFreePokemon(UserEntity user, double number) {
        PokemonEntity pokemon = new UserDAO().getFreePokemon(number);
        pokemon = new PokemonDAO().savePokemon(pokemon, user.getId());
        return pokemon;
    }
}
