package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;

    public Item addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        cart.add(item);
        log.info("item with id " + itemId + " has been added to the card");
        return item;
    }
}
