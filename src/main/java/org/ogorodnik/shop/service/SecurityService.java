package org.ogorodnik.shop.service;

import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class SecurityService {
    private final List<String> sessionList = Collections.synchronizedList(new ArrayList<>());
    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public String allowLogin(String userName, String password) throws SQLException {
        log.info("Check if user password is correct and user can login");
        String passwordFromDatabase = userService.getUserPassword(userName);
        if (null != passwordFromDatabase) {
            if (BCrypt.checkpw(password, passwordFromDatabase)) {
                String uuid = UUID.randomUUID().toString();
                sessionList.add(uuid);
                log.info("login is successful");
                return uuid;
            } else {
                log.info("Login failed. Password is incorrect");
                return null;
            }
        } else {
            log.info("login failed. User does not exist");
            return null;
        }
    }

    public boolean logout(String uuid) {
        if (sessionList.contains(uuid)) {
            sessionList.remove(uuid);
            log.info("user has been logged out successfully");
            return true;
        }
        log.info("Something went wrong. User can't be found and was not logged out");
        return false;
    }

    public boolean validateIfLoggedIn(String uuid) {
        log.info("validate if user is logged in");
        return sessionList.contains(uuid);
    }
}
