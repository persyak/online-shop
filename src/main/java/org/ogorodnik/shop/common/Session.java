package org.ogorodnik.shop.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Builder
@Getter
@RequiredArgsConstructor
public class Session {
    private final String username;
    private final List<Item> cart = new CopyOnWriteArrayList<>();

    //TODO: what is better to use instead of Date? spring.security.Timestamp?
    private final Date date;
}
