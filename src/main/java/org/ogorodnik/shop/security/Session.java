package org.ogorodnik.shop.security;

import lombok.Getter;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.entity.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Session {
    private final String userToken;
    private final LocalDateTime expireDate;
    private final List<Item> cart = new CopyOnWriteArrayList<>();

    private final Credentials credentials;

    public Session(String userToken, LocalDateTime expireDate, Credentials credentials) {
        this.userToken = userToken;
        this.expireDate = expireDate;
        this.credentials = credentials;
    }
}
