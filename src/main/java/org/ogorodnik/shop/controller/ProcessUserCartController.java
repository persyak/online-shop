package org.ogorodnik.shop.controller;

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

    @GetMapping({"/userCart", "/userCart/*"})
    @ResponseBody
    protected String getUserCart(@RequestAttribute Session session) {
        log.info("got user session. processing user card");
        List<Item> cart = session.getCart();
        if (cart.size() > 0) {
            return pageGenerator.getPage("userCart.html", Map.of("items", cart));
        }
        return pageGenerator.getPage("userCartIsEmpty.html");
    }

    @RequestMapping(path = {"/userCart", "/userCart/*"}, method = RequestMethod.POST)
    protected String addToUserCart(HttpServletRequest request) {
        String uri = request.getRequestURI();
        //TODO: not sure it's the best option to use substring to get item id and parse it.
        String stringId = uri.substring(10);
        long productId = Long.parseLong(stringId);

        Session session = (Session) request.getAttribute("session");
        List<Item> cart = session.getCart();
        cartService.addToCart(cart, productId);
        log.info("item with id " + productId + " has been added to the card");
        return "redirect:/items";
    }
}
