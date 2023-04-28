package com.uca.dao;

import com.uca.dao._Connector;
import com.uca.dao._Generic;
import com.uca.entity.UserEntity;
import com.uca.entity.PokemonEntity;

import java.util.Random;
import java.sql.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UserDAO extends _Generic<UserEntity> {

    public void test() {
        try {
            ResultSet result;
            PreparedStatement request;
            request = connect.prepareStatement(
                    "INSERT INTO user (email,userName, firstname, lastname, password,lastFreePokemon) VALUES ('email','userName','firstName','lastName','password',TO_DATE('10/10/1990','DD/MM/YYYY'));");
            request.executeUpdate();

            request = connect.prepareStatement(
                    "SELECT id,email,firstName,LastName,password,lastFreePokemon FROM user;");
            result = request.executeQuery();
            while (result.next()) {
                System.out.println(result.getDate("lastFreePokemon"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PokemonEntity getFreePokemon(double number) {
        Random rand = new Random();
        boolean find = false;
        Integer id;
        id = (rand.nextInt() % 1008) + 1;

        while (!find) {

            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon-species/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                /* Getting the response code */

                int responsecode = conn.getResponseCode();

                if (responsecode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responsecode);
                } else {

                    String inline = "";
                    Scanner scanner = new Scanner(url.openStream());

                    /* Write all the JSON data into a string using a scanner */
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }

                    /* Using the JSON simple library parse the string into a json object */

                    JSONParser parse = new JSONParser();
                    JSONObject data_obj = (JSONObject) parse.parse(inline);

                    boolean legendary = (boolean) data_obj.get("is-legendary");
                    boolean mythical = (boolean) data_obj.get("is-mythical");

                    /* Get the required object from the above created object */
                    if (number <= 0.005 && mythical) {
                        find = true;
                    } else if (number <= 0.01 && legendary) {
                        find = true;
                    } else {
                        find = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            id = (rand.nextInt() % 1008) + 1;
        }
        PokemonEntity newPokemon = pokemonSpecies(id);
        newPokemon = pokemon(newPokemon);
        return newPokemon;
    }

    private PokemonEntity pokemonSpecies(Integer id) {

        /* Connection in url = https://pokeapi.co/api/v2/pokemon-species/id */
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon-species/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            /* Getting the response code */

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                /* Write all the JSON data into a string using a scanner */
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                /* Using the JSON simple library parse the string into a json object */

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                /* Create new pokemon with API */

                PokemonEntity pokemon = new PokemonEntity();
                pokemon.setIdAPI(id);
                pokemon.setLevel(1);
                JSONArray name = (JSONArray) data_obj.get("names");

                for (int i = 0; i < name.size(); i++) {
                    JSONObject object = (JSONObject) name.get(i);
                    object = (JSONObject) object.get("language");
                    if (object.get("name") == "en") {
                        JSONObject pokemonName = (JSONObject) name.get(i);
                        pokemon.setName((String) pokemonName.get("name"));
                        break;
                    }
                }
                return pokemon;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PokemonEntity pokemon(PokemonEntity pokemon) {
        /* Connection in url = https://pokeapi.co/api/v2/pokemon/id */

        try {
            Integer id = pokemon.getIdAPI();
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            /* Getting the response code */

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                /* Write all the JSON data into a string using a scanner */
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                /* Using the JSON simple library parse the string into a json object */

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                /* Complete pokemon with API */

                /* Get sprites in API */

                JSONObject sprites = (JSONObject) data_obj.get("sprites");
                Random rand = new Random();
                double value = rand.nextDouble();
                if (value < 0.05) {
                    pokemon.setSprite((String) sprites.get("front_shiny"));
                } else {
                    pokemon.setSprite((String) sprites.get("front_default"));
                }

                /* Get types in API */

                JSONArray types = (JSONArray) data_obj.get("types");

                for (int i = 0; i < types.size(); i++) {
                    JSONObject type = (JSONObject) types.get(i);
                    type = (JSONObject) type.get("type");
                    pokemon.setType((String) type.get("name"));
                }
                return pokemon;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserEntity connection(UserEntity obj) {
        try {
            PreparedStatement request;
            ResultSet result;
            request = connect.prepareStatement(
                    "SELECT id,email,firstName,LastName,password,lastFreePokemon FROM user where userName = ?;");
            request.setString(1, obj.getUserName());
            result = request.executeQuery();
            while (result.next()) {
                if (result.getString("password") == obj.getPassword()) {
                    obj.setId(result.getInt("id"));
                    obj.setEmail(result.getString("email"));
                    obj.setFirstName(result.getString("firstName"));
                    obj.setLastName(result.getString("lastName"));
                    obj.setLastFreePokemon(result.getDate("lastFreePokemon"));
                    obj.setConnection(true);
                }
            }
            return obj;
        } catch (SQLException e) {
            e.printStackTrace();
            obj.setConnection(false);
            return obj;
        }

    }

    public UserEntity create(UserEntity obj) {

        Integer id = -1;
        obj.setId(id);
        try {
            PreparedStatement request;
            request = connect.prepareStatement("INSERT INTO user VALUES(?,?,?,?,?);");
            request.setString(1, obj.getEmail());
            request.setString(2, obj.getUserName());
            request.setString(3, obj.getFirstName());
            request.setString(4, obj.getLastName());
            request.setString(5, obj.getPassword());
            request.executeQuery();
            request = connect.prepareStatement("SELECT id FROM user where email = ?;");
            request.setString(1, obj.getEmail());
            ResultSet resultSet = request.executeQuery();
            if (resultSet.next()) {
                obj.setId(resultSet.getInt("id"));
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
}