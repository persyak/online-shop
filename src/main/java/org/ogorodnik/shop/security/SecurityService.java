package org.ogorodnik.shop.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityService {
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();

    @Value("${session.cookie.max.age}")
    private int sessionMaxAge;
    @Autowired
    private final UserService userService;

    public Optional<Session> login(Credentials credentials) {
        log.info("Check if user password is correct and user can login");
        EncryptedPassword encryptedPassword = userService.getUserPassword(credentials.getName());
        String hashPasswordFromUi = BCrypt.hashpw(credentials.getPassword(), encryptedPassword.getSalt());
        if (hashPasswordFromUi.equals(encryptedPassword.getPassword())) {
            LocalDateTime expireDate =
                    LocalDateTime.now().plusSeconds(sessionMaxAge);
            Session session = new Session(UUID.randomUUID().toString(), expireDate);
            sessionList.add(session);
            log.info("login is successful");
            return Optional.of(session);
        }
        log.info("Login failed. Password is incorrect or user was not found");
        return Optional.empty();
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
                }
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }
}
