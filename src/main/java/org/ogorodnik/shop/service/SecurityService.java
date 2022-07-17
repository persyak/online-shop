package org.ogorodnik.shop.service;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private final List<String> sessionList = new ArrayList<>();
    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public String allowLogin(String userName, String password) throws SQLException {
        String passwordFromDatabase = userService.getUserPassword(userName);
        if (passwordFromDatabase != null && BCrypt.checkpw(password, passwordFromDatabase)) {
            String uuid = UUID.randomUUID().toString();
            sessionList.add(uuid);
            return uuid;
        } else {
            return null;
        }
    }

    public boolean logout(String uuid) {
        if (sessionList.contains(uuid)) {
            sessionList.remove(uuid);
            return true;
        }
        return false;
    }

    public boolean validateIfLoggedIn(String uuid) {
        return sessionList.contains(uuid);
    }
}
