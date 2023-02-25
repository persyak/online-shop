package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;
    private final SecurityService securityService;

    public Optional<Item> addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        if (null != item) {
            cart.add(item);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    public Optional<Session> getSession(String userToken) {
        return securityService.getSession(userToken);
    }

}
