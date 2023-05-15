package com.uca.security;

import com.uca.core.PokemonCore;
import com.uca.core.UserCore;
import com.uca.entity.PokemonEntity;
import com.uca.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.UUID;


public class doLogin {
    // Note : In real life, this line should be (and must be !) in a configuration file, not in source code !
    private final static String TOKEN = "QVAlKTzo1zW9VwfGvJtrFZiSOzzEzEyb4Q4qdYIYncKqhd4l7Iasgq8LbesvH01Jk8kA49HNt9fq4M4Lpjpjvysyso7egZNlmHSU";

    public static UserEntity introspect(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {

            Claims claims = Jwts.parser().setSigningKey(TOKEN).parseClaimsJws(token).getBody();

            UserEntity entity = new UserEntity();
            entity.setUserName(claims.get("emitter", String.class));
            entity.setId(claims.get("uuid", Integer.class));
            entity = UserCore.getById(entity.getId());
            ArrayList<PokemonEntity> pokemon = PokemonCore.getAllPokemon(entity.getId());
            if (pokemon != null) {
                entity.setPokemon(pokemon);
            }
            return entity;
        } catch (Exception e) {
            return null;
        }
    }


    public static String login(UserEntity entity) throws IllegalArgumentException {

        if (entity.getUserName() == null || entity.getUserName().isEmpty()) {
            return null;
        }
        if (entity.getPassword() == null || entity.getPassword().isEmpty()) {
            return null;
        }

        UserEntity user;
        try {
            user = UserCore.connection(entity);
            if (user == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        /*if (!BCrypt.checkpw(entity.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }*/

        //Could connect as application
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", user.getId());
        map.put("id", user.getId());
        map.put("emitter", user.getUserName());

        HashMap<String, Object> header = new HashMap<>();
        header.put("kid", user.getId());

        //Generating user token for required service

        return Jwts.builder()
                .setClaims(map)
                .setId(UUID.randomUUID().toString())
                .setHeader(header)
                .setHeaderParam("kid", user.getId())
                .setSubject(user.getUserName())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 20))
                .signWith(SignatureAlgorithm.HS512, TOKEN)
                .compact();
    }
}
