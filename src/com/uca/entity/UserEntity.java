package com.uca.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;

import com.uca.core.UserCore;
import com.uca.dao.UserDAO;

import java.util.Random;

public class UserEntity {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date lastFreePokemon;
    private String userName;
    private ArrayList<PokemonEntity> pokemon;
    private boolean connection;

    public UserEntity() {
        this.pokemon = new ArrayList<PokemonEntity>();
    }

    /* GETTEUR */

    public Integer getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public Date getLastFreePokemon() {
        return this.lastFreePokemon;
    }

    /* SETTEUR */

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastFreePokemon(Date lastFreePokemon) {
        this.lastFreePokemon = lastFreePokemon;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    /* METHOD */

    public void test() {
        UserCore.test();
    }

    public boolean connection(String userName, String password) {
        this.password = password;
        this.userName = userName;
        UserCore.connection(this);
        if (this.id >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean register(String firstName, String lastName, String userName, String email, String password,
                            String comfirmPassword) {

        if (password == comfirmPassword) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.userName = userName;
            this.connection = false;
            UserCore.register(this);

            if (this.id < 0) {

                return false;

            } else {

                return true;

            }
        } else {

            return false;

        }
    }

    public boolean freePokemon() {
        Date today = new Date();
        if (today.getDate() != this.lastFreePokemon.getDate() || today.getMonth() != this.lastFreePokemon.getMonth()
                || today.getYear() != this.lastFreePokemon.getYear()) {
            Random random = new Random();
            double number;
            number = random.nextDouble();
            PokemonEntity newPokemon = UserCore.getFreePokemon(this, number);
            if (newPokemon != null) {
                this.pokemon.add((newPokemon));
                this.lastFreePokemon = today;
                return true;
            }
            return false;
        } else {
            return false;
        }

    }
}
