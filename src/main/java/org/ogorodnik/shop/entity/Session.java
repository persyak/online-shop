package org.ogorodnik.shop.entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Session {
    private final String userToken;
    private final LocalDateTime expireDate;
    private final List<Long> itemIds = new ArrayList<>();

    public Session(String uuid, LocalDateTime expireDate) {
        this.userToken = uuid;
        this.expireDate = expireDate;
    }

    public void addItemToTheCard(long id) {
        itemIds.add(id);
    }
}
