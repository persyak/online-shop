package org.ogorodnik.shop.service;

import org.ogorodnik.shop.web.security.PasswordManager;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class, Object> SERVICES = new HashMap<>();

    static{
        SERVICES.put(ItemService.class, new ItemService());
        SERVICES.put(PageGenerator.class, new PageGenerator());
        //TODO: SERVICES.put(JdbcItemDao.class, new JdbcItemDao(new ConnectionFactory()));
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

    public static <T> T getService(Class<T> clazz){
        return clazz.cast(SERVICES.get(clazz));
    }
}
