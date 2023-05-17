package com.uca.gui;

import com.uca.core.UserCore;
import com.uca.entity.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class UserGUI {

    public static String displayUser(UserEntity user) throws IOException, TemplateException {

        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();
        input.put("user", user);
        input.put("pokemon", user.getPokemon());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/user.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String displayOtherUser(UserEntity user) throws IOException, TemplateException {

        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();
        input.put("user", user);
        input.put("pokemon", user.getPokemon());

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/profilOtherUser.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String displayAllUser(UserEntity user) throws IOException, TemplateException {

        Configuration configuration = _FreeMarkerInitializer.getContext();


        HashMap<String, Object> input = new HashMap<>();
        input.put("users", UserCore.getAllUser(user.getId()));

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/users.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

}
