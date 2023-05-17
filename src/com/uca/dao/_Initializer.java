package com.uca.dao;

import com.uca.core.PokemonCore;

import java.sql.*;

public class _Initializer {

    public static void Init() {
        Connection connection = _Connector.getInstance();

        try {

            PreparedStatement user;
            PreparedStatement market;
            PreparedStatement pokemon;
            PreparedStatement type;
            PreparedStatement pokedex;
            PreparedStatement exchange;

            /* Create table user */

            /*exchange = connection.prepareStatement(
                    "DROP TABLE exchange;"
            );
            exchange.executeUpdate();

            market = connection.prepareStatement(
                    " DROP TABLE market;");
            market.executeUpdate();

            pokedex = connection.prepareStatement(
                    "DROP TABLE pokedex;"
            );
            pokedex.executeUpdate();

            type = connection.prepareStatement(
                    "DROP TABLE type;"
            );
            type.executeUpdate();

            pokemon = connection.prepareStatement(
                    " DROP TABLE pokemon;");
            pokemon.executeUpdate();

            user = connection.prepareStatement(
                    " DROP TABLE user;");
            user.executeUpdate();*/

            user = connection.prepareStatement(
                    " CREATE TABLE IF NOT EXISTS user (id int auto_increment, email varchar(100),userName varchar(50), firstname varchar(100), lastname varchar(100), password varchar(100),lastFreePokemon long,lastPexing long, pexing int, PRIMARY KEY(id,email,userName));");
            user.executeUpdate();

            /* Create table type */
            type = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS type (idType int auto_increment, types varchar(20), PRIMARY KEY (idType))");
            type.executeUpdate();

            /* Create table pokedex */
            pokedex = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS pokedex (idAPI int primary key, name varchar(30), rarity varchar(20), idType1 int, idType2 int, sprite varchar(200), shinySprite varchar(200), FOREIGN KEY (idType1) REFERENCES type(idType), FOREIGN KEY (idType2) REFERENCES type(idType));"
            );
            pokedex.executeUpdate();

            /* Create table pokemon */
            pokemon = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS pokemon (idPokemon int primary key auto_increment,idOwner int, idAPI int, level int,shiny int, FOREIGN KEY (idOwner) REFERENCES user(id), FOREIGN KEY (idAPI) REFERENCES pokedex(idAPI))");
            pokemon.executeUpdate();

            /* Create table market */
            market = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS market (id int primary key auto_increment , idOwner int,FOREIGN KEY (idOwner) REFERENCES user(id))");
            market.executeUpdate();

            exchange = connection.prepareStatement(
                    "CREATE TABLE  IF NOT EXISTS exchange(idExchange int, idPokemonWanted int, idPokemonTrade int, shiny int, PRIMARY KEY(idExchange,idPokemonWanted,idPokemonTrade), FOREIGN KEY(idExchange) REFERENCES market(id), FOREIGN KEY(idPokemonWanted) REFERENCES pokedex(idAPI),FOREIGN KEY(idPokemonTrade) REFERENCES pokemon(idPokemon) )"
            );
            exchange.executeUpdate();

            //PokemonCore.addAllPokemon();

        } catch (Exception e) {
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
