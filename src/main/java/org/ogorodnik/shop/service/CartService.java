package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;

    public Optional<Item> addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        if (null != item) {
            cart.add(item);
            return Optional.of(item);
        }
        return Optional.empty();
    }
}
