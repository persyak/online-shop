package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CartService {

    @Autowired
    private final ItemService itemService;

    public void addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        cart.add(item);
    }
}
