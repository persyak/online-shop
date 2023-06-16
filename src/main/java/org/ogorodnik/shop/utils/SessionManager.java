package org.ogorodnik.shop.utils;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Session;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SessionManager {

    private final List<Session> sessionList = new CopyOnWriteArrayList<>();

    public Session getSession(String username) {
        for (Session session : sessionList) {
            if (username.equals(session.getUsername()) && session.getDate().after(new Date())) {
                return session;
            }
            if (username.equals(session.getUsername()) && session.getDate().before(new Date())) {
                sessionList.remove(session);
            }
        }
        Session session = Session.builder()
                .username(username)
                .date(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .build();
        sessionList.add(session);
        return session;
    }
}
