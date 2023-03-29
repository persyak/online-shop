package org.ogorodnik.shop.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();

    private final UserService userService;
    private int sessionMaxAge;

    public Optional<Session> login(Credentials credentials) {
        log.info("Check if user password is correct and user can login");
        Optional<EncryptedPassword> encryptedPassword = userService.getUserPassword(credentials.getLogin());

        if (encryptedPassword.isPresent() &&
                credentialsEqualPassword(credentials, encryptedPassword.get())) {
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

    private boolean credentialsEqualPassword(
            Credentials credentials, EncryptedPassword encryptedPassword) {
        return BCrypt.hashpw(credentials.getPassword(), encryptedPassword.getSalt())
                .equals(encryptedPassword.getPassword());
    }

    @Value("${session.cookie.max.age}")
    public void setSessionMaxAge(int sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
    }
}
