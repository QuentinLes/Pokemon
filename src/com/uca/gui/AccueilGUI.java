package com.uca.gui;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class AccueilGUI {
    public static String display() throws IOException, TemplateException {

        Configuration configuration = _FreeMarkerInitializer.getContext();

        HashMap<String, Object> input = new HashMap<>();

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/accueil.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
}
