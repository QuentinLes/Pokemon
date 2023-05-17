package com.uca.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;

import com.uca.core.PokemonCore;
import com.uca.core.UserCore;
import com.uca.dao.UserDAO;
import org.eclipse.jetty.websocket.api.SuspendToken;

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
    private Date lastLevelUp;
    private Integer levelUpPerDay;

    private Integer numberPokemon;
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

    public Date getLastLevelUp() {
        return this.lastLevelUp;
    }

    public Integer getLevelUpPerDay() {
        return this.levelUpPerDay;
    }

    public ArrayList<PokemonEntity> getPokemon() {
        return this.pokemon;
    }

    public Integer getNumberPokemon() {
        return this.numberPokemon;
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

    public void setPokemon(ArrayList<PokemonEntity> pokemon) {
        this.pokemon = pokemon;
    }

    public void setLastLevelUp(Date date) {
        this.lastLevelUp = date;
    }

    public void setLevelUpPerDay(Integer levelUpPerDay) {
        this.levelUpPerDay = levelUpPerDay;
    }

    public void setNumberPokemon(Integer numberPokemon) {
        this.numberPokemon = numberPokemon;
    }

    /* METHOD */

    public boolean register(String firstName, String lastName, String userName, String email, String password,
                            String confirmPassword) {

        if (password.equals(confirmPassword)) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.userName = userName;
            this.lastFreePokemon = new Date(0l);
            System.out.println(this.lastFreePokemon);
            this.connection = false;
            UserCore.register(this);
            System.out.println(this.id);
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
            PokemonEntity newPokemon = UserCore.getFreePokemon(this);
            if (newPokemon != null) {
                this.pokemon.add((newPokemon));
                this.lastFreePokemon = today;
                UserCore.saveLastFreePokemon(this);
                return true;
            }
        }
        return false;
    }

    public boolean levelUp(Integer idPokemon) {
        Date date = new Date();
        if (date.getYear() != this.lastLevelUp.getYear() || date.getMonth() != this.lastLevelUp.getMonth() || date.getDate() != this.lastLevelUp.getDate()) {
            this.levelUpPerDay = 0;
        }
        for (int i = 0; i < pokemon.size(); i++) {
            PokemonEntity p = pokemon.get(i);
            if (p.getIdPokemon().equals(idPokemon) && this.levelUpPerDay < 5) {
                if (p.levelUp()) {
                    this.lastLevelUp = new Date();
                    this.levelUpPerDay += 1;
                    UserCore.saveLevel(lastLevelUp, levelUpPerDay, id);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean levelUp(UserEntity user, Integer idPokemon) {

        for (int i = 0; i < user.getPokemon().size(); i++) {
            PokemonEntity p = user.getPokemon().get(i);
            if (p.getIdPokemon().equals(idPokemon) && this.levelUpPerDay < 5) {
                if (p.levelUp()) {
                    this.lastLevelUp = new Date();
                    this.levelUpPerDay += 1;
                    UserCore.saveLevel(lastLevelUp, levelUpPerDay, id);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
