package org.ogorodnik.shop.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProcessUserCartController {

    private final CartService cartService;

    @GetMapping({"/userCart", "/userCart/{productId}"})
    protected String getUserCart(@RequestAttribute Session session,
                                 Model model) {
        log.info("got user session. processing user card");
        List<Item> cart = session.getCart();
        if (cart.size() > 0) {
            model.addAttribute("items", cart);
            return "userCart";
        }
        return "userCartIsEmpty";
    }

    @RequestMapping(path = {"/userCart", "/userCart/{productId}"}, method = RequestMethod.POST)
    protected String addToUserCart(@PathVariable long productId, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");
        List<Item> cart = session.getCart();
        cartService.addToCart(cart, productId);
        log.info("item with id " + productId + " has been added to the card");
        return "redirect:/items";
    }
}
