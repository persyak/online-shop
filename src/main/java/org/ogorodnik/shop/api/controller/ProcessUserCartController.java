package org.ogorodnik.shop.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.error.SessionNotFoundException;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProcessUserCartController {

    private final CartService cartService;

    @GetMapping("/api/v1/userCart")
    protected List<Item> getUserCart(@RequestParam String userToken) throws SessionNotFoundException {
        Session session = cartService.getSession(userToken);
        log.info("got user session. processing user card");
        return session.getCart();
    }

    @PostMapping("/api/v1/userCart/{productId}")
    protected Item addToUserCart(@PathVariable long productId, @RequestParam String userToken)
            throws ItemNotFountException, SessionNotFoundException {
        List<Item> cart = cartService.getSession(userToken).getCart();
        return cartService.addToCart(cart, productId);
    }
}
