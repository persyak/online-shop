package org.ogorodnik.shop.web.templater;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class PageGenerator {
    private final Map<String, Object> defaultDataMap = new HashMap<>();

    public String getPage(String filename, Map<String, Object> data) {
        Writer writer = new StringWriter();
        try {
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setClassForTemplateLoading(PageGenerator.class, "/");
            configuration.setDefaultEncoding("UTF-8");
            Template template = configuration.getTemplate(filename);
            template.process(data, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    public String getPage(String filename) {
        Writer writer = new StringWriter();
        try {
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setClassForTemplateLoading(PageGenerator.class, "/");
            configuration.setDefaultEncoding("UTF-8");
            Template template = configuration.getTemplate(filename);
            template.process(defaultDataMap, writer);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

}
