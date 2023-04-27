package com.uca.dao;

import com.uca.dao._Connector;
import com.uca.dao._Generic;
import com.uca.entity.UserEntity;
import com.uca.entity.PokemonEntity;
import java.sql.*;
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

        while (!find) {

            id = (rand.nextInt() % 1008) + 1;

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

                /* Get the required object from the above created object */
                if (number <= 0.005 && data_obj.getBoolean("is-mythical")) {

                    find = true;

                } else if (number <= 0.01 && data_obj.getBoolean("is-legendary")) {

                    find = true;

                } else {
                    find = true;
                }
            }
        }

        return pokemon(id);
    }

    private PokemonEntity pokemon(Integer id) {
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

            PokemonEntity pokemon = new PokemonEntity();
            // a finir apres la classe pokemon

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