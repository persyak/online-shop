package org.ogorodnik.shop.service;

import org.ogorodnik.shop.entity.Item;

import java.util.List;

public interface CartService {

    Item addToCart(long itemId);

    List<Item> getCart();
}
