package com.uca.dao;


import com.uca.core.PokemonCore;
import com.uca.entity.UserEntity;
import com.uca.entity.PokemonEntity;

import java.util.*;
import java.sql.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UserDAO extends _Generic<UserEntity> {

    public PokemonEntity getFreePokemon() {
        Random rand = new Random();
        boolean find = false;
        Integer id;
        id = (rand.nextInt(1008)) + 1;
        PokemonEntity newPokemon;
        Float shiny = rand.nextFloat();
        if (shiny < 0.05) {

            newPokemon = PokemonCore.getPokemonByIdAPI(id, 1);
            newPokemon.setIdAPI(id);
            newPokemon.setShiny(1);
        } else {
            newPokemon = PokemonCore.getPokemonByIdAPI(id, 0);
            newPokemon.setIdAPI(id);
            newPokemon.setShiny(0);
        }
        if (newPokemon == null) {
            return null;
        }

        return newPokemon;
    }

    public UserEntity connection(UserEntity obj) {
        try {
            PreparedStatement request;
            ResultSet result;
            request = connect.prepareStatement(
                    "SELECT id,email,firstName,LastName,password,lastFreePokemon FROM user where userName = ?;");
            request.setString(1, obj.getUserName());
            result = request.executeQuery();
            if (result.next()) {

                if (result.getString("password").equals(obj.getPassword())) {
                    obj.setId(result.getInt("id"));
                    obj.setEmail(result.getString("email"));
                    obj.setFirstName(result.getString("firstName"));
                    obj.setLastName(result.getString("lastName"));
                    obj.setLastFreePokemon(new Date(result.getLong("lastFreePokemon")));
                    obj.setConnection(true);
                    return obj;
                }
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            obj.setConnection(false);
            return null;
        }

    }

    public UserEntity create(UserEntity obj) {
        System.out.println("create");
        Integer id = -1;
        obj.setId(id);
        try {
            PreparedStatement request;
            request = connect.prepareStatement("INSERT INTO user (email,userName,firstName,lastName,password,lastFreePokemon) VALUES(?,?,?,?,?,?);");
            request.setString(1, obj.getEmail());
            request.setString(2, obj.getUserName());
            request.setString(3, obj.getFirstName());
            request.setString(4, obj.getLastName());
            request.setString(5, obj.getPassword());
            request.setLong(6, obj.getLastFreePokemon().getTime());
            request.executeUpdate();
            request = connect.prepareStatement("SELECT id FROM user where email = ?;");
            request.setString(1, obj.getEmail());
            ResultSet resultSet = request.executeQuery();
            if (resultSet.next()) {
                obj.setId(resultSet.getInt("id"));
                System.out.println("Ajout validee");
                return obj;
            }
            obj.setId(id);
            return obj;
        } catch (SQLException e) {
            e.printStackTrace();
            return obj;
        }

    }

    public boolean delete(UserEntity obj) {
        try {
            PreparedStatement request = this.connect.prepareStatement("DELETE INTO user where id=?;");
            request.setInt(1, obj.getId());
            request.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public UserEntity getById(Integer id) {
        try {
            UserEntity user = new UserEntity();
            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT email,firstName,lastName,userName,lastFreePokemon,lastPexing,pexing FROM user where id = ?;"
            );
            request.setInt(1, id);
            ResultSet result = request.executeQuery();
            if (result.next()) {
                user.setEmail(result.getString("email"));
                user.setFirstName(result.getString("firstName"));
                user.setLastName(result.getString("lastName"));
                user.setLastFreePokemon(new Date(result.getLong("lastFreePokemon")));
                user.setUserName(result.getString("userName"));
                user.setLastLevelUp(new Date(result.getLong("lastPexing")));
                user.setLevelUpPerDay(result.getInt("pexing"));
                user.setId(id);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserEntity getByUserName(String username) {
        try {
            UserEntity user = new UserEntity();
            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT id,email,firstName,lastName,userName,lastFreePokemon,lastPexing,pexing FROM user where userName = ?;"
            );
            request.setString(1, username);
            ResultSet result = request.executeQuery();
            if (result.next()) {
                user.setEmail(result.getString("email"));
                user.setFirstName(result.getString("firstName"));
                user.setLastName(result.getString("lastName"));
                user.setLastFreePokemon(new Date(result.getLong("lastFreePokemon")));
                user.setUserName(result.getString("userName"));
                user.setLastLevelUp(new Date(result.getLong("lastPexing")));
                user.setLevelUpPerDay(result.getInt("pexing"));
                user.setId(result.getInt("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveLastFreePokemon(UserEntity user) {
        try {
            PreparedStatement request = this.connect.prepareStatement("UPDATE user set lastFreePokemon = ? where id = ?;");
            request.setLong(1, user.getLastFreePokemon().getTime());
            request.setInt(2, user.getId());
            request.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveLevel(Date date, Integer levelUpPerDay, Integer id) {
        try {
            PreparedStatement request = this.connect.prepareStatement(
                    "UPDATE user SET lastPexing = ?, pexing = ? where id=?;"
            );
            request.setLong(1, date.getTime());
            request.setInt(2, levelUpPerDay);
            request.setInt(3, id);
            request.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserEntity> getAllUsers(Integer id) {
        ArrayList<UserEntity> users = new ArrayList<>();
        try {
            PreparedStatement user = this.connect.prepareStatement(
                    "SELECT id,userName,count(*) as number FROM user INNER JOIN pokemon on user.id = pokemon.idOwner GROUP BY id;"
            );
            ResultSet results = user.executeQuery();
            while (results.next()) {
                UserEntity newUser = new UserEntity();
                newUser.setId(results.getInt("id"));
                newUser.setUserName(results.getString("userName"));
                newUser.setNumberPokemon(results.getInt("number"));
                users.add(newUser);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}