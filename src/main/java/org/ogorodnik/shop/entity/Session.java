package org.ogorodnik.shop.entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Session {
    private String uuid;
    private LocalDateTime expireDate;
    private final List<Long> card = new ArrayList<>();

    public Session(String uuid, LocalDateTime expireDate) {
        this.uuid = uuid;
        this.expireDate = expireDate;
    }

    public void addItemToTheCard(long id) {
        card.add(id);
    }
}
