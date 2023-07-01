package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.entity.Credentials;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.common.Session;
import org.ogorodnik.shop.exception.TokenNotFoundException;
import org.ogorodnik.shop.utils.SessionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;
    private final SessionManager sessionManager;

    //TODO: the cart can be saved into repository (I do not know what is used in commercial projects)
    public Item addToCart(long itemId) {
        Item item = itemService.getItemById(itemId);
        getSession(getUsername()).getCart().add(item);
        log.info("item with id " + itemId + " has been added to the card");
        return item;
    }

    public List<Item> getCart() {
        return getSession(getUsername()).getCart();
    }

    private Session getSession(String username) {
        return sessionManager.getSession(username);
    }

    private String getUsername() {
        try {
            Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
            Credentials credentials = (Credentials) authToken.getPrincipal();
            return credentials.getUsername();
        } catch (NullPointerException exception) {
            throw new TokenNotFoundException("Token was not found. Ensure user is authorised");
        }
    }
}
