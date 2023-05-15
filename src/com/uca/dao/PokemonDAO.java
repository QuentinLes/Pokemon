package com.uca.dao;

import com.uca.entity.PokemonEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PokemonDAO extends _Generic<PokemonEntity> {

    public PokemonEntity savePokemon(PokemonEntity pokemon, Integer userId) {
        Integer id = -1;
        pokemon.setIdPokemon(id);
        try {

            PreparedStatement addPokemon;
            ResultSet result;
            addPokemon = connect.prepareStatement(
                    "Insert INTO pokemon (idOwner,idAPI,level,shiny) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            addPokemon.setInt(1, userId);
            addPokemon.setInt(2, pokemon.getIdAPI());
            addPokemon.setInt(3, pokemon.getLevel());
            addPokemon.setInt(4, pokemon.getShiny());


            addPokemon.executeUpdate();
            result = addPokemon.getGeneratedKeys();
            result.next();
            pokemon.setIdPokemon(result.getInt(1));
            return pokemon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveLevel(Integer level, Integer id) {
        try {
            PreparedStatement save = this.connect.prepareStatement("UPDATE pokemon set level = ?  where idPokemon = ?;");
            save.setInt(1, level);
            save.setInt(2, id);
            save.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addType(String type) {
        try {
            PreparedStatement add = connect.prepareStatement("INSERT INTO type (types) VALUES (?);");
            add.setString(1, type);
            add.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PokemonEntity> getAllPokemon(Integer idOwner) throws Exception {
        try {
            ArrayList<PokemonEntity> pokemon = new ArrayList<>();
            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT idPokemon, idAPI,level,shiny FROM pokemon where idOwner = ?;"
            );
            request.setInt(1, idOwner);
            ResultSet result = request.executeQuery();
            while (result.next()) {
                PokemonEntity pok = new PokemonEntity();
                pok.setIdPokemon(result.getInt("idPokemon"));
                pok.setLevel(result.getInt("level"));
                pok.setIdAPI(result.getInt("idAPI"));
                pok.setShiny(result.getInt("shiny"));

                PreparedStatement infoPokemon;
                infoPokemon = this.connect.prepareStatement(
                        "SELECT sprite,shinySprite,name,idType1,idType2 FROM pokedex WHERE idAPI = ?;"
                );
                infoPokemon.setInt(1, pok.getIdAPI());
                result = infoPokemon.executeQuery();
                result.next();

                pok.setType(result.getString("idType1"));
                if (!result.getString("idType2").equals("null")) {
                    pok.setType(result.getString("idType2"));
                }
                if (pok.getShiny() == 0) {
                    pok.setSprite(result.getString("sprite"));
                } else {
                    pok.setSprite(result.getString("shinySprite"));
                }
                pok.setName(result.getString("name"));
                pokemon.add(pok);
            }
            return pokemon;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAllName() {

        PokemonEntity pokemon;
        ArrayList<String> names = new ArrayList<>();
        try {
            PreparedStatement request = this.connect.prepareStatement(
                    "SELECT name FROM pokedex;"
            );
            ResultSet result = request.executeQuery();
            while (result.next()) {
                names.add(result.getString("name"));
            }
            return names;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PokemonEntity create(PokemonEntity obj) {
        return null;
    }

    @Override
    public boolean delete(PokemonEntity obj) {
        return false;
    }

    public PokemonEntity getPokemonById(Integer idPokemon) {
        try {

            PokemonEntity pokemon = new PokemonEntity();
            PreparedStatement requestPokemon = this.connect.prepareStatement(
                    "SELECT idAPI,level,shiny FROM pokemon where idPokemon = ?; "
            );
            requestPokemon.setInt(1, idPokemon);
            ResultSet result = requestPokemon.executeQuery();
            if (result.next()) {

                pokemon.setIdAPI(result.getInt("idAPI"));
                pokemon.setIdPokemon(idPokemon);
                pokemon.setLevel(result.getInt("level"));
                pokemon.setShiny(result.getInt("shiny"));

                PreparedStatement requestPokedex = this.connect.prepareStatement(
                        "SELECT sprite,shinySprite,name,idType1,idType2 FROM pokedex WHERE idAPI =?"
                );
                requestPokemon.setInt(1, pokemon.getIdAPI());
                ResultSet resultPokedex = requestPokedex.executeQuery();
                resultPokedex.next();
                if (pokemon.getShiny() == 0) {
                    pokemon.setSprite(resultPokedex.getString("sprite"));
                } else {
                    pokemon.setSprite(resultPokedex.getString("shinySprite"));
                }
                pokemon.setName(resultPokedex.getString("name"));
                pokemon.setType(resultPokedex.getString("idType1"));
                if (!resultPokedex.getString("idType2").equals("null")) {
                    pokemon.setType(resultPokedex.getString("idType2"));
                }
                return pokemon;
            }
            return null;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }
    }

    public PokemonEntity getPokemonByIdAPI(Integer idPokemon, Integer shiny) {

        PokemonEntity pokemon = new PokemonEntity();
        try {
            PreparedStatement requestPokedex = this.connect.prepareStatement(
                    "SELECT sprite,shinySprite,name,idType1,idType2 FROM pokedex WHERE idAPI =?"
            );
            requestPokedex.setInt(1, idPokemon);
            ResultSet resultPokedex = requestPokedex.executeQuery();
            System.out.println("test");
            System.out.println(idPokemon);
            if (resultPokedex.next()) {

                if (shiny == 0) {
                    pokemon.setSprite(resultPokedex.getString("sprite"));
                } else {
                    pokemon.setSprite(resultPokedex.getString("shinySprite"));
                }
                pokemon.setName(resultPokedex.getString("name"));

                pokemon.setType(resultPokedex.getString("idType1"));
                System.out.println("test2");
                if (!resultPokedex.getString("idType2").equals("null")) {
                    pokemon.setType(resultPokedex.getString("idType2"));
                }
                return pokemon;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addAllPokemonInPokedex() {
        PokemonEntity newPokemon;
        for (int i = 982; i > 0; i--) {
            System.out.println(i);
            System.out.println("test");
            newPokemon = pokemonSpecies(i);
            System.out.println("test1");
            newPokemon = pokemon(newPokemon);
            System.out.println(newPokemon.getType());
            System.out.println("test2");

            Integer idType[] = new Integer[2];
            idType[0] = null;
            idType[1] = null;
            String type;
            Integer compteur = 0;
            PreparedStatement requestType;
            ResultSet result;

            String sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + i + ".png";
            String shinySprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + i + ".png";

            try {

                /* Verify if pokemon type is on database */

                Iterator<String> types = newPokemon.getType().iterator();
                while (types.hasNext()) {
                    requestType = connect.prepareStatement("Select idType FROM type where types = ?");
                    System.out.println("test3");
                    type = types.next();
                    requestType.setString(1, type);
                    result = requestType.executeQuery();
                    if (!result.next()) {
                        System.out.println("test3.1");
                        addType(type);
                        requestType = connect.prepareStatement("Select idType FROM type where types = ?");
                        requestType.setString(1, type);
                        result = requestType.executeQuery();
                        result.next();
                    }
                    idType[compteur] = result.getInt("idType");
                    compteur++;
                }
                if (idType[1] == null) {
                    PreparedStatement save = this.connect.prepareStatement(
                            "INSERT INTO pokedex (idAPI,name,rarity,idType1,sprite,shinySprite) VALUES(?,?,?,?,?,?);"
                    );
                    System.out.println("test4");
                    save.setInt(1, i);
                    save.setString(2, newPokemon.getName());
                    save.setString(3, newPokemon.getSprite());
                    save.setInt(4, idType[0]);
                    save.setString(5, sprite);
                    save.setString(6, shinySprite);
                    save.executeUpdate();
                } else {
                    PreparedStatement save = this.connect.prepareStatement(
                            "INSERT INTO pokedex (idAPI,name,rarity,idType1,idType2,sprite,shinySprite) VALUES(?,?,?,?,?,?,?);"
                    );
                    System.out.println("test4");
                    save.setInt(1, i);
                    save.setString(2, newPokemon.getName());
                    save.setString(3, newPokemon.getSprite());
                    save.setInt(4, idType[0]);
                    save.setInt(5, idType[1]);
                    save.setString(6, sprite);
                    save.setString(7, shinySprite);
                    save.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public PokemonEntity pokemonSpecies(Integer id) {

        /* Connection in url = https://pokeapi.co/api/v2/pokemon-species/id */
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon-species/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla");
            conn.setRequestMethod("GET");
            conn.connect();

            /* Getting the response code */

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                System.out.println("HttpResponseCode: " + responsecode);
                return null;
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
                    if (object.get("name").equals("en")) {
                        JSONObject pokemonName = (JSONObject) name.get(i);
                        pokemon.setName((String) pokemonName.get("name"));
                        break;
                    }
                }

                boolean legendary = (boolean) data_obj.get("is_legendary");
                boolean mythical = (boolean) data_obj.get("is_mythical");

                if (legendary) {
                    pokemon.setSprite("legendary");
                } else if (mythical) {
                    pokemon.setSprite("mythical");
                } else {
                    pokemon.setSprite("common");
                }
                return pokemon;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PokemonEntity pokemon(PokemonEntity pokemon) {
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
                System.out.println("HttpResponseCode: " + responsecode);
                return null;
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
}
