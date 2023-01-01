package org.ogorodnik.shop.security;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class SecurityService {
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();

    private final int sessionMaxAge;
    private final UserService userService;

    public SecurityService(UserService userService, int sessionMaxAge) {
        this.userService = userService;
        this.sessionMaxAge = sessionMaxAge;
    }

    public Optional<Session> login(Credentials credentials) {
        log.info("Check if user password is correct and user can login");
        EncryptedPassword encryptedPassword = userService.getUserPassword(credentials.getUserName());
        String hashPasswordFromUi = BCrypt.hashpw(credentials.getPassword(), encryptedPassword.getSalt());
        if (hashPasswordFromUi.equals(encryptedPassword.getPassword())) {
            LocalDateTime expireDate =
                    LocalDateTime.now().plusSeconds(sessionMaxAge);
            Session session = new Session(UUID.randomUUID().toString(), expireDate);
            sessionList.add(session);
            log.info("login is successful");
            return Optional.of(session);
        } else {
            log.info("Login failed. Password is incorrect or user was not found");
            return Optional.empty();
        }
    }

    public boolean logout(String uuid) {
        for (Session session : sessionList) {
            if (uuid.equals(session.getUserToken())) {
                sessionList.remove(session);
                log.info("user has been logged out successfully");
                return true;
            }
        }
        log.info("Something went wrong. User can't be found and was not logged out");
        return false;
    }

    public Optional<Session> getSession(String userToken) {
        log.info("validate if user is logged in");
        for (Session session : sessionList) {
            if (userToken.equals(session.getUserToken())) {
                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                    sessionList.remove(session);
                    return Optional.empty();
                } else {
                    return Optional.of(session);
                }
            }
        }
        return Optional.empty();
    }
}
