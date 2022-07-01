package org.ogorodnik.shop.service;

import jakarta.servlet.http.Cookie;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private List<String> sessionList = new ArrayList<>();
    private UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Cookie allowLogin(String userName, String password) throws SQLException {
        String passwordFromDatabase = userService.getUserPassword(userName);
        if (passwordFromDatabase != null && BCrypt.checkpw(password, passwordFromDatabase)) {
            String uuid = UUID.randomUUID().toString();
            sessionList.add(uuid);
            return new Cookie("user-token", uuid);
        } else {
            return null;
        }
    }

    public boolean logout(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    if (sessionList.contains(cookie.getValue())) {
                        sessionList.remove(cookie.getValue());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean validateIfLoggedIn(Cookie[] cookies) {
        boolean isValid = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    if (sessionList.contains(cookie.getValue())) {
                        isValid = true;
                    }
                    break;
                }
            }
        }
        return isValid;
    }
}
