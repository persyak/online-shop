package org.ogorodnik.shop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Builder
@Getter
@RequiredArgsConstructor
public class Session {
    private final String username;
    private final List<Item> cart = new CopyOnWriteArrayList<>();
    private final Date date;
}
