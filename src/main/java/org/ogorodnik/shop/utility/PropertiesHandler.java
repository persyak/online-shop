package org.ogorodnik.shop.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PropertiesHandler {
    private static final Map<String, Properties> cachedProperties = new ConcurrentHashMap<>();
    private static final String DEFAULT_PROPERTIES_PATH = "conf/application.properties";

    public static Properties getDefaultProperties() {
        cachedProperties.putIfAbsent(DEFAULT_PROPERTIES_PATH, readProperties(DEFAULT_PROPERTIES_PATH));
        return new Properties(cachedProperties.get(DEFAULT_PROPERTIES_PATH));
    }

    private static Properties readProperties(String path) {
        Properties properties = new Properties();
        try (BufferedInputStream resource = new BufferedInputStream
                (Objects.requireNonNull(PropertiesHandler.class.getClassLoader().getResourceAsStream(path)))) {
            properties.load(resource);
            return properties;
        } catch (IOException e) {
            log.error("Cannot read properties from file: {} ", path, e);
            throw new RuntimeException("Cannot read properties from file: " + path, e);
        }
    }
}
