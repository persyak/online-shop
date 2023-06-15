package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.entity.Session;
import org.ogorodnik.shop.utils.SessionManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemService itemService;
    private final SessionManager sessionManager;

    //TODO: the cart can be saved into repository (I do not know what is used in commercial projects)
    public Item addToCart(long itemId, String username) {
        Item item = itemService.getItemById(itemId);
        getSession(username).getCart().add(item);
        log.info("item with id " + itemId + " has been added to the card");
        return item;
    }

    public List<Item> getCart(String username) {
        return getSession(username).getCart();
    }

    private Session getSession(String username) {
        return sessionManager.getSession(username);
    }
}
