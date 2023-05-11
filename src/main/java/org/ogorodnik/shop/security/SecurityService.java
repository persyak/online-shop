package org.ogorodnik.shop.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.entity.Credentials;
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

    public Session login(Credentials credentials) throws AuthenticationException {
        log.info("Check if user password is correct and user can login");
        Credentials credentialsFromDb = userService.getCredentials(credentials.getLogin());

        if (!credentialsEqualPassword(credentials, credentialsFromDb)) {
            throw new AuthenticationException("Password is not correct");
        }
        Session session = createSession(credentialsFromDb);
        sessionList.add(session);
        log.info("login is successful");
        return session;
    }

    public void logout(String uuid) {
        for (Session session : sessionList) {
            if (uuid.equals(session.getUserToken())) {
                sessionList.remove(session);
                log.info("user has been logged out successfully");
            }
        }
        log.info("Something went wrong. User can't be found and was not logged out");
    }

    public Optional<Session> createSession(String userToken) {
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

    private boolean credentialsEqualPassword(Credentials credentials, Credentials credentialsFromDb) {
        return BCrypt.hashpw(credentials.getPassword(), credentialsFromDb.getSalt())
                .equals(credentialsFromDb.getPassword());
    }

    @Value("${session.cookie.max.age}")
    public void setSessionMaxAge(int sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
    }

    private Session createSession(Credentials credentials) {
        LocalDateTime expireDate =
                LocalDateTime.now().plusSeconds(sessionMaxAge);
        return new Session(UUID.randomUUID().toString(), expireDate, credentials);
    }
}
