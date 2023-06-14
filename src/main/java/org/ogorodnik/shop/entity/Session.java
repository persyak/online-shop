package org.ogorodnik.shop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@RequiredArgsConstructor
public class Session {
    private final String username;
    private final List<Item> cart = new CopyOnWriteArrayList<>();
}
