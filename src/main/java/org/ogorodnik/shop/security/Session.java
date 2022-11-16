package org.ogorodnik.shop.security;

import lombok.Getter;
import org.ogorodnik.shop.entity.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Session {
    private final String userToken;
    private final LocalDateTime expireDate;
    private final List<Item> cart = new ArrayList<>();

    public Session(String userToken, LocalDateTime expireDate) {
        this.userToken = userToken;
        this.expireDate = expireDate;
    }
}
