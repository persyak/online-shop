package org.ogorodnik.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.entity.Session;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class SecurityService {
    private final List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private ItemService itemService;
    private final UserService userService;

    public SecurityService(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    public Session allowLogin(String userName, String password) throws SQLException {
        log.info("Check if user password is correct and user can login");
        List<String> credentialsList = userService.getUserPassword(userName);
        if (credentialsList.size() == 2) {
            String hashPasswordFromUi = BCrypt.hashpw(password, credentialsList.get(1));
            if (hashPasswordFromUi.equals(credentialsList.get(0))) {
                Session session = new Session(UUID.randomUUID().toString(), LocalDateTime.now().plusHours(4));
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
            if (uuid.equals(session.getUuid())) {
                iterator.remove();
                log.info("user has been logged out successfully");
                return true;
            }
        }
        log.info("Something went wrong. User can't be found and was not logged out");
        return false;
    }

    public boolean validateIfLoggedIn(String uuid) {
        log.info("validate if user is logged in");
        Iterator<Session> iterator = sessionList.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (uuid.equals(session.getUuid())) {
                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                    iterator.remove();
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    //TODO: how to avoid using if when we know that session is available
    // for sure and if has been already checked and there is no null value
    public Session getSession(String uuid) {
        return sessionList.stream()
                .filter(session -> uuid.equals(session.getUuid()))
                .findAny()
                .orElse(null);
    }

    public List<Item> getCard(List<Long> card) throws SQLException {
        return itemService.getCard(card);
    }
}
