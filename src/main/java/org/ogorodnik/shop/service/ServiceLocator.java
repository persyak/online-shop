package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.dao.jdbc.HikariDataSourceFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.dao.jdbc.JdbcUserDao;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.templater.PageGenerator;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceLocator {
    private static final Map<Class, Object> SERVICES = new HashMap<>();
    private static final Properties properties;

    static {
        properties = PropertiesHandler.getDefaultProperties();

        DataSource dataSource = HikariDataSourceFactory.create(properties);
        ItemDao itemDao = new JdbcItemDao(dataSource);
        UserDao userDao = new JdbcUserDao(dataSource);
        UserService userService = new UserService(userDao);
        ItemService itemService = new ItemService(itemDao);
        SecurityService securityService = new SecurityService(userService);
        CartService cartService = new CartService(itemService);

        SERVICES.put(ItemService.class, itemService);
        SERVICES.put(PageGenerator.class, new PageGenerator());
        SERVICES.put(SecurityService.class, securityService);
        SERVICES.put(UserService.class, userService);
        SERVICES.put(CartService.class, cartService);
    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(SERVICES.get(clazz));
    }

    public static Properties getProperties() {
        return properties;
    }
}
