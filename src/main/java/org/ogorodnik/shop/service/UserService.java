package org.ogorodnik.shop.service;

import lombok.AllArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<String> getUserPassword(String name) throws SQLException {
        return userDao.getUserPassword(name);
    }

    public void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        userDao.updatePasswordAndSalt(encryptedPassword, salt, login);
    }
}
