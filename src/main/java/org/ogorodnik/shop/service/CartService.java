package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.exception.SessionNotFoundException;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;
    private final SecurityService securityService;

    public Item addToCart(List<Item> cart, long itemId) {
        Item item = itemService.getItemById(itemId);
        cart.add(item);
        log.info("item with id " + itemId + " has been added to the card");
        return item;
    }

    public Session getSession(String userToken) {
        Optional<Session> sessionOptional = securityService.createSession(userToken);

        if (sessionOptional.isPresent()) {
            return sessionOptional.get();
        }
        throw new SessionNotFoundException("Session not found");
    }
}
