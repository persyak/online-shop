package org.ogorodnik.shop.security;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.service.UserService;
import org.ogorodnik.shop.utility.PropertiesHandler;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class SecurityService {
    private final List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());

    private final Properties properties = PropertiesHandler.getDefaultProperties();

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Session allowLogin(String userName, String password) {
        log.info("Check if user password is correct and user can login");
        List<String> credentialsList = userService.getUserPassword(userName);
        if (credentialsList.size() == 2) {
            String hashPasswordFromUi = BCrypt.hashpw(password, credentialsList.get(1));
            if (hashPasswordFromUi.equals(credentialsList.get(0))) {
                LocalDateTime expireDate =
                        LocalDateTime.now().plusSeconds(Long.parseLong(properties.getProperty("session.cookie.max.age")));
                Session session = new Session(UUID.randomUUID().toString(), expireDate);
                sessionList.add(session);
                log.info("login is successful");
                return session;
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
        Iterator<Session> iterator = sessionList.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (uuid.equals(session.getUserToken())) {
                iterator.remove();
                log.info("user has been logged out successfully");
                return true;
            }
        }
        log.info("Something went wrong. User can't be found and was not logged out");
        return false;
    }

    public Optional<Session> getSession(String userToken) {
        log.info("validate if user is logged in");
        Iterator<Session> iterator = sessionList.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (userToken.equals(session.getUserToken())) {
                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                    iterator.remove();
                    return Optional.empty();
                } else {
                    return Optional.of(session);
                }
            }
        }
        return Optional.empty();
    }
}
