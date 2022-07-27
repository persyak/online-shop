package org.ogorodnik.shop.web.security;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.service.UserService;

import java.sql.SQLException;

@AllArgsConstructor
public class PasswordManager {

    private final UserService userService;

    private String encryptPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    private void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        userService.updatePasswordAndSalt(encryptedPassword, salt, login);
    }

    public void setPasswordAndSalt(String login) throws SQLException {
        String salt = BCrypt.gensalt();
        String encryptedPassword = encryptPassword(login, salt);
        updatePasswordAndSalt(encryptedPassword, salt, login);
    }
}
