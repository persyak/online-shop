package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.dao.jdbc.ConnectionFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcUserDao;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDao userDao = new JdbcUserDao(ConnectionFactory.getInstance());

    public List<String> getUserPassword(String name) throws SQLException {
        return userDao.getUserPassword(name);
    }

    public void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        userDao.updatePasswordAndSalt(encryptedPassword, salt, login);
    }
}
