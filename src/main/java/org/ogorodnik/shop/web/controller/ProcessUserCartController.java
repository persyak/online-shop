package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProcessUserCartController {

    private final CartService cartService;

    @GetMapping("/api/v1/userCart")
    protected List<Item> getUserCart(@RequestParam String userToken) {
        Optional<Session> sessionOptional = cartService.getSession(userToken);
        if (sessionOptional.isPresent()) {
            log.info("got user session. processing user card");
            List<Item> cart = sessionOptional.get().getCart();
            if (cart.size() > 0) {
                return cart;
            }
        }
        return List.of();
    }

    @PostMapping("/api/v1/userCart/{productId}")
    protected List<Item> addToUserCart(@PathVariable long productId, @RequestParam String userToken) {
        Optional<Session> sessionOptional = cartService.getSession(userToken);
        if (sessionOptional.isPresent()) {
            List<Item> cart = sessionOptional.get().getCart();
            log.info("item with id " + productId + " has been added to the card");
            Optional<Item> optionalItem = cartService.addToCart(cart, productId);
            if (optionalItem.isPresent()) {
                return List.of(optionalItem.get());
            }
        }
        return List.of();
    }
}
