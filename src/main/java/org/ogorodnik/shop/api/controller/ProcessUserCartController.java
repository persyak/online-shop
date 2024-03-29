package org.ogorodnik.shop.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class ProcessUserCartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    protected List<Item> getUserCart() {
        return cartService.getCart();
    }

    @PostMapping("/item/{itemId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    protected Item addToUserCart(@PathVariable long itemId) {
        return cartService.addToCart(itemId);
    }
}
