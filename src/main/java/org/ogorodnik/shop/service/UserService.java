package org.ogorodnik.shop.service;

import lombok.Setter;
import org.ogorodnik.shop.dao.UserDao;

import java.sql.SQLException;
import java.util.List;

@Setter
public class UserService {
    private UserDao userDao;

    public List<String> getUserPassword(String name) throws SQLException {
        return userDao.getUserPassword(name);
    }

    public void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        userDao.updatePasswordAndSalt(encryptedPassword, salt, login);
    }
}
