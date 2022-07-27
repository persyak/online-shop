package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.UserDao;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public List<String> getUserPassword(String name) throws SQLException {
        return userDao.getUserPassword(name);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        userDao.updatePasswordAndSalt(encryptedPassword, salt, login);
    }
}
