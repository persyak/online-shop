package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.UserDao;

import java.sql.SQLException;

public class UserService {
    private UserDao userDao;

    public String getUserPassword(String name) throws SQLException {
        return userDao.getUserPassword(name);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
