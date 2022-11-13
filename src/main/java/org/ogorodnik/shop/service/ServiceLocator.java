package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.ConnectionFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.security.PasswordManager;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceLocator {
    private static final Map<Class, Object> SERVICES = new HashMap<>();
    private static final Properties properties;

    static {
        properties = PropertiesHandler.getProperties();
        SERVICES.put(ItemDao.class, new JdbcItemDao(ConnectionFactory.getInstance()));
        SERVICES.put(ItemService.class, new ItemService());
        SERVICES.put(PageGenerator.class, new PageGenerator());
        SERVICES.put(UserService.class, new UserService());
        SERVICES.put(SecurityService.class, new SecurityService(
                getService(UserService.class), getService(ItemService.class)));

        PasswordManager passwordManager = new PasswordManager(getService(UserService.class));
        try {
            passwordManager.setPasswordAndSalt("atrubin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            passwordManager.setPasswordAndSalt("oohorodnik");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(SERVICES.get(clazz));
    }

    public static Properties getProperties() {
        return properties;
    }
}
