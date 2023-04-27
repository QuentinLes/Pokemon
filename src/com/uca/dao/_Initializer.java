package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init() {
        Connection connection = _Connector.getInstance();

        try {

            PreparedStatement user;
            PreparedStatement market;
            PreparedStatement pokemon;

            /* Create table user */

            /*
             * user = connection.prepareStatement(
             * " DROP TABLE market;");
             * user.executeUpdate();
             * 
             * user = connection.prepareStatement(
             * " DROP TABLE pokemon;");
             * user.executeUpdate();
             * 
             * user = connection.prepareStatement(
             * " DROP TABLE user;");
             * user.executeUpdate();
             */

            user = connection.prepareStatement(
                    " CREATE TABLE IF NOT EXISTS user (id int auto_increment, email varchar(100),userName varchar(50), firstname varchar(100), lastname varchar(100), password varchar(100),lastFreePokemon date, PRIMARY KEY(id,email,userName));");
            user.executeUpdate();

            /* Create table pokemon */
            pokemon = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS pokemon (idPokemon int primary key auto_increment,idProprietaire int, idAPI int, level int, shiny boolean, FOREIGN KEY (idProprietaire) REFERENCES user(id))");
            pokemon.executeUpdate();

            /* Create table market */
            market = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS market (idProp int, idPokemonEchanger int, idPokemonVoulut int, PRIMARY KEY (idProp,idPokemonEchanger,idPokemonVoulut),FOREIGN KEY (idProp) REFERENCES user(id),FOREIGN KEY (idPokemonEchanger) REFERENCES pokemon(idPokemon))");
            market.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
