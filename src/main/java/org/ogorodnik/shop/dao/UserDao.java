package org.ogorodnik.shop.dao;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    List<String> getUserPassword(String name) throws SQLException;

    void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException;
}
