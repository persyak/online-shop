package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.error.SessionNotFoundException;
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

    public Item addToCart(List<Item> cart, long itemId) throws ItemNotFountException {
        Item item = itemService.getItemById(itemId);
        cart.add(item);
        return item;
    }

    public Session getSession(String userToken) throws SessionNotFoundException {
        Optional<Session> sessionOptional = securityService.getSession(userToken);

        if (sessionOptional.isPresent()) {
            return sessionOptional.get();
        }
        throw new SessionNotFoundException("Session not found");
    }
}
