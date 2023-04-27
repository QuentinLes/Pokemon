package com.uca;

import com.uca.dao._Initializer;
import com.uca.gui.*;
import com.uca.entity.UserEntity;

import src.com.uca.gui.AccueilGUI;

import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        // Configure Spark
        staticFiles.location("/static/");
        port(8081);

        _Initializer.Init();

        get("/test", (req, res) -> {
            UserEntity test = new UserEntity();
            test.test();
            return AccueilGUI.display();
        });

        // Page d'accueil

        get("/", (req, res) -> {
            return AccueilGUI.display();
        });

        // Page de connexion

        get("/login", (req, res) -> {
            return LoginGUI.displayLogin();
        });

        // Connexion au site, renvoie la page de l'utilisateur inscrit

        post("/login", (req, res) -> {
            String userName = req.queryParams("userName");
            String password = req.queryParams("password");
            UserEntity user = new UserEntity();
            user.connection(userName, password);
            if (user.getId() >= 0) {
                return UserGUI.displayUser(user.getId());
            }
            return LoginGUI.displayLogin(); // Voir pour mettre un message d'erreur
        });

        // Page d'inscription
        get("/register", (req, res) -> {
            return RegisterGUI.displayRegister();
        });

        // Inscription au site, renvoie la page d'accueil du site
        post("/register", (req, res) -> {
            String userName = req.queryParams("userName");
            String firstName = req.queryParams("fistName");
            String lastName = req.queryParams("lastname");
            String email = req.queryParams("email");
            String password = req.queryParams("password");
            String comfirmPassword = req.queryParams("comfirmPassword");
            boolean success;
            UserEntity user = new UserEntity();
            success = user.register(firstName, lastName, userName, email, password, comfirmPassword);
            if (success) {
                return AccueilGUI.display(); // Mettre un message de succes
            } else {
                return RegisterGUI.displayRegister(); // Mettre un message d'erreur
            }
        });

        // Page d'accueil de l'utilisateur a id ?
        get("/user/:userId", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            return UserGUI.displayUser(userId);
        });

        // Regarder le profil d'un autre utilisateur avec l'id
        get("/user/:userId/profil/:userId2", (req, res) -> {
            Integer userId = Integer.valueOf(req.queryParams(":userId2"));
            return UserGUI.displayUser(userId);
        });

        // Pexer un pokemon
        post("/user/:userId", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            Integer pokemonId = Integer.valueOf(req.queryParams("pokemonId"));
            PokemonPexingGUI.pexer(userId, userId, pokemonId);
            return UserGUI.displayUser(userId);
        });

        post("/user/:userId/profil/:userId2", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            Integer userId2 = Integer.valueOf(req.params(":userId2"));
            Integer pokemonId = Integer.valueOf(req.queryParams("pokemonId"));
            PokemonPexingGUI.pexer(userId, userId2, pokemonId);
            return UserGUI.displayUser(userId);
        });

        // Obtenir le market
        get("/user/:userId/market", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            return MarketGUI.displayMarket();
        });

        // Ajouter un echange dans le market
        get("/user/:user.id}/market/add", (req, res) -> {
            return MarketGUI.displayMarketAdd();
        });

        post("/user/:user.id}/market/add", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            Integer pokemonIdExchange = Integer.valueOf(req.queryParams("idPokemonExchange"));
            Integer pokemonIdRequire = Integer.valueOf(req.queryParams("idPokemonRequire"));
            if (MarketGUI.addExchange(userId, pokemonIdExchange, pokemonIdRequire)) {
                return MarketGUI.displayMarket(); // Message de succes
            } else {
                return MarketGUI.displayMarketAdd(); // Message de refus
            }
        });

        // Fais l'echanger (1 pour 1) avec un utilisateur
        post("/user/:userId/market/exchange", (req, res) -> {
            Integer userId = Integer.valueOf(req.params(":userId"));
            Integer userId2 = Integer.valueOf(req.queryParams("userId2"));
            Integer pokemon1 = Integer.valueOf(req.queryParams("pokemon1"));
            Integer pokemon2 = Integer.valueOf(req.queryParams("pokemon2"));
            MarketGUI.exchange(userId, userId2, pokemon1, pokemon2);
            return MarketGUI.displayMarket();
        });

    }
}