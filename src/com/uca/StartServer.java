package com.uca;

import com.uca.core.MarketCore;
import com.uca.core.PokemonCore;
import com.uca.core.UserCore;
import com.uca.dao.MarketDAO;
import com.uca.dao.PokemonDAO;
import com.uca.entity.MarketEntity;
import com.uca.entity.PokemonEntity;
import com.uca.security.doLogin;
import com.uca.dao._Initializer;
import com.uca.gui.*;
import com.uca.entity.UserEntity;

import com.uca.gui.AccueilGUI;
import spark.Request;
import spark.Session;

import javax.servlet.http.HttpSession;

import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        // Configure Spark
        staticFiles.location("/static/");
        port(8081);

        _Initializer.Init();

        // Page d'accueil

        get("/", (req, res) -> {
            return AccueilGUI.display();
        });

        // Page de connexion

        get("/login", (req, res) -> {
            String msg = req.queryParams("msg");
            return LoginGUI.displayLogin(msg);
        });

        // Connexion au site, renvoie la page de l'utilisateur inscrit

        post("/login", (req, res) -> {
            String userName = req.queryParams("userName");
            String password = req.queryParams("password");
            UserEntity user = new UserEntity();
            user.setUserName(userName);
            user.setPassword(password);
            try {
                String log = doLogin.login(user);
                if (log == null) {
                    res.redirect("/login?msg=Invalid user name or password", 303);
                    return null;
                }
                res.cookie("/", "auth", log, 36000, false, true);
            } catch (Exception e) {
                res.redirect("/", 303);
                return null;
            }
            res.redirect("/profil", 303);
            return null;
        });

        // Page d'inscription
        get("/register", (req, res) -> {
            return RegisterGUI.displayRegister();
        });

        // Inscription au site, renvoie la page d'accueil du site
        post("/register", (req, res) -> {
            String userName = req.queryParams("userName");
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            String email = req.queryParams("email");
            String password = req.queryParams("password");
            String confirmPassword = req.queryParams("confirmPassword");
            System.out.println(userName);
            boolean success;
            UserEntity user = new UserEntity();
            success = user.register(firstName, lastName, userName, email, password, confirmPassword);
            if (success) {
                System.out.println("inscription validee");
                res.status(200);
                return AccueilGUI.display(); // Mettre un message de succes
            } else {
                System.out.println("inscription refusee");
                res.status(407);
                return RegisterGUI.displayRegister(); // Mettre un message d'erreur
            }
        });

        // Logout
        post("/logout", (req, res) -> {
            res.removeCookie("auth");
            res.redirect("/", 301);
            return null;
        });

        // Page d'accueil de l'utilisateur a id ?
        get("/profil", (req, res) -> {

            UserEntity connectedUser = getAuthenticatedUser(req);

            if (connectedUser == null) {
                res.redirect("/login");
                res.status(303);
                return null;
            } else {
                return UserGUI.displayUser(connectedUser);
            }
        });

        post("/user/freePokemon", (req, res) -> {

            UserEntity connectedUser = getAuthenticatedUser(req);

            if (connectedUser == null) {
                res.redirect("/login");
                res.status(301);
                return null;
            }
            if (connectedUser.freePokemon()) {
                res.redirect("/profil", 303);
                return null;
            }
            res.status(503);
            res.redirect("/profil");
            return UserGUI.displayUser(connectedUser);
        });

        // User list in site
        get("/userList", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login");
                res.status(301);
                return null;
            }
            return UserGUI.displayAllUser(connectedUser);
        });

        // Regarder le profil d'un autre utilisateur avec l'id
        get("/profil/:userName", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login", 301);
                return null;
            }
            String userName = req.params(":userName");
            UserEntity profil = new UserEntity();
            profil = UserCore.getByUserName(userName);
            profil.setPokemon(PokemonCore.getAllPokemon(profil.getId()));
            return UserGUI.displayOtherUser(profil);
        });

        // Pexer un pokemon
        post("/profil", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            System.out.println(connectedUser.getLastLevelUp());
            System.out.println(connectedUser.getLevelUpPerDay());
            if (connectedUser == null) {
                res.redirect("/login", 303);
                return null;
            } else {
                Integer pokemonId = Integer.valueOf(req.queryParams("idPokemon"));
                if (connectedUser.levelUp(pokemonId)) {
                    return UserGUI.displayUser(connectedUser);
                } else {
                    res.status(503);
                    return UserGUI.displayUser(connectedUser);
                }

            }
        });

        post("/profil/:userName", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login", 303);
                return null;
            }
            Integer pokemonId = Integer.valueOf(req.queryParams("idPokemon"));
            String userName = req.params(":userName");
            UserEntity profil = new UserEntity();
            profil = UserCore.getByUserName(userName);
            profil.setPokemon(PokemonCore.getAllPokemon(profil.getId()));
            if (connectedUser.levelUp(profil, pokemonId)) {
                return UserGUI.displayOtherUser(profil);
            }
            res.status(503);
            return UserGUI.displayOtherUser(profil);
        });

        // Obtenir le market
        get("/market", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login", 303);
                return null;
            }
            return MarketGUI.displayMarket();
        });

        // Ajouter un echange dans le market
        get("/market/add", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login", 303);
                return null;
            }
            return MarketGUI.displayMarketAdd(connectedUser);
        });

        post("/market/add", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {
                res.redirect("/login", 303);
                return null;
            }
            String pokemonExchange = req.queryParams("pokemonExchange");
            Integer id = Integer.valueOf(pokemonExchange);
            PokemonEntity pokemonEx = PokemonCore.getPokemonById(id);
            if (pokemonEx == null) {
                res.redirect("/market?msg = error with pokemon exchange");
                return null;
            }

            String shiny = req.queryParams("shiny");
            Integer shi;
            if (shiny == "on") {
                shi = 1;
            } else {
                shi = 0;
            }
            String pokemonWanted = req.queryParams("pokemonWanted");
            Integer idAPI = Integer.valueOf(pokemonWanted);
            PokemonEntity pokemonWan = PokemonCore.getPokemonByIdAPI(idAPI, 0);
            if (pokemonWan == null) {
                res.redirect("/market?msg = error with pokemon wanted");
                return null;
            }

            MarketEntity market = new MarketEntity();
            String msg = market.createExchange(connectedUser.getId(), pokemonEx, pokemonWan, shi);
            if (msg != null) {
                res.redirect("/market? msg=" + msg, 303);
                return null;
            }
            res.redirect("/market", 303);
            return null;
        });


        // Page de l'echange
        get("/market/exchange", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {

                res.redirect("/login", 303);
                return null;

            }
            Integer idPokemon = Integer.valueOf(req.queryParams("idPokemon"));
            Integer id = Integer.valueOf(req.queryParams("id"));

            MarketEntity exchange = MarketCore.getExchangeById(id);
            if (exchange == null) {
                res.redirect("/market?msg = Error, exchange not found", 303);
                return null;
            }

            return MarketGUI.displayMarketExchange(connectedUser, exchange);
        });

        // Fais l'echanger (1 pour 1) avec un utilisateur
        post("/market/exchange", (req, res) -> {
            UserEntity connectedUser = getAuthenticatedUser(req);
            if (connectedUser == null) {

                res.redirect("/login", 303);
                return null;

            }

            Integer idExchange = Integer.valueOf(req.queryParams("idExchange"));
            Integer pokemon1 = Integer.valueOf(req.queryParams("pokemonExchange"));
            MarketEntity exchange = MarketCore.getExchangeById(idExchange);
            String msg = MarketCore.exchange(exchange.getIdOwner(), exchange.getExchangedPokemon().getIdPokemon(), connectedUser.getId(), pokemon1, idExchange);
            if (msg == null) {
                res.redirect("/market", 303);
                return null;
            }
            res.redirect("/market?msg = " + msg, 303);
            return null;
        });

    }

    /*
     * Verify if user is already connected
     * @param (Request) request HTTP
     * Return UserEntity object if is it connected, else null
     */
    private static UserEntity getAuthenticatedUser(Request req) {
        String token = req.cookie("auth");
        return token == null ? null : doLogin.introspect(token);
    }

}