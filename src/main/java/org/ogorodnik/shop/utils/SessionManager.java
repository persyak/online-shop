package org.ogorodnik.shop.utils;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class SessionManager {

    private final List<Session> sessionList = new CopyOnWriteArrayList<>();

    //TODO: think how to remove session when token is expired.
    public Session getSession(String username) {
        for (Session session: sessionList){
            if (username.equals(session.getUsername())){
                return session;
            }
        }
        Session session = new Session(username);
        sessionList.add(session);
        return session;
    }
}
