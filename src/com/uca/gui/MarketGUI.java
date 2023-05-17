package com.uca.gui;

import com.uca.core.MarketCore;
import com.uca.core.PokemonCore;
import com.uca.entity.MarketEntity;
import com.uca.entity.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class MarketGUI {
    public static String displayMarket() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();
        input.put("market", MarketCore.getAllExchange());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/market.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String displayMarketAdd(UserEntity user) throws Exception {

        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();
        input.put("pokemonUser", PokemonCore.getAllPokemon(user.getId()));
        input.put("allPokemon", PokemonCore.getAllName());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/addExchange.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String displayMarketExchange(UserEntity user, MarketEntity exchange) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();
        input.put("pokemonUser", PokemonCore.getAllPokemonWithIdAPI(user.getId(), exchange.getWantedPokemon().getIdAPI(), exchange.getShiny()));
        input.put("idExchange", exchange.getId());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/exchange.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

}
