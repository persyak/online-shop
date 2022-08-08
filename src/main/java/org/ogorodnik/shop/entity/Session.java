package org.ogorodnik.shop.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Session {
    private String uuid;
    private LocalDateTime expireDate;

    public List<Item> card;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
