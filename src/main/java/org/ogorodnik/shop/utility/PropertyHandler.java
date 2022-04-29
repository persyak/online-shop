package org.ogorodnik.shop.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {

    public static Properties readConfigPropery(String fileName) {
        Properties property = new Properties();
        //I have a question here: why FileInputStream does not work here?
        try (InputStream inputStream = PropertyHandler.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                property.load(inputStream);
            } else {
                throw new RuntimeException(
                        new FileNotFoundException("property file " + fileName + " was not found in the classpath"));
            }
        } catch (IOException exception) {
            throw new RuntimeException("property file " + fileName + " was not found in the classpath");
        }
        return property;
    }
}
