package org.ogorodnik.shop.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.common.Session;
import org.ogorodnik.shop.exception.TokenNotFoundException;
import org.ogorodnik.shop.service.CartService;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCartService implements CartService {

    //TODO: I'd like cart to be deleted when session is expired,
    // therefore I added session and get cart from session
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    private final ItemService itemService;

    public Item addToCart(long itemId) {
        Item item = itemService.getItemById(itemId);
        getSession(getUsername()).getCart().add(item);
        log.info("item with id {} has been added to the card", itemId);
        return item;
    }

    public List<Item> getCart() {
        return getSession(getUsername()).getCart();
    }

    private Session getSession(String username) {
        for (Session session : sessionList) {
            if (username.equals(session.getUsername()) && session.getDate().after(new Date())) {
                return session;
            }
            if (username.equals(session.getUsername()) && session.getDate().before(new Date())) {
                sessionList.remove(session);
            }
        }
        Session session = Session.builder()
                .username(username)
                .date(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .build();
        sessionList.add(session);
        return session;
    }

    private String getUsername() {
        Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
        if (null == authToken) {
            throw new TokenNotFoundException("Token was not found. Ensure user is authorised");
        }
        return authToken.getPrincipal().toString();
    }
}
