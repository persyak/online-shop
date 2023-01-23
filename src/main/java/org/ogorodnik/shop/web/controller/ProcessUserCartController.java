package org.ogorodnik.shop.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ProcessUserCartController {

    private final CartService cartService;
    private final PageGenerator pageGenerator;

    @Autowired
    public ProcessUserCartController(final CartService cartService, final PageGenerator pageGenerator) {
        this.cartService = cartService;
        this.pageGenerator = pageGenerator;
    }

    @GetMapping({"/userCart", "/userCart/{productId}"})
    @ResponseBody
    protected String getUserCart(@RequestAttribute Session session) {
        log.info("got user session. processing user card");
        List<Item> cart = session.getCart();
        if (cart.size() > 0) {
            return pageGenerator.getPage("userCart.html", Map.of("items", cart));
        }
        return pageGenerator.getPage("userCartIsEmpty.html");
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
