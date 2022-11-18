package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;

import java.util.List;

@RequiredArgsConstructor
public class CartService {
    private final ItemService itemService;

    public void addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        cart.add(item);
    }
}
