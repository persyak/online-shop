package org.ogorodnik.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ProcessUserCartController {

    private final CartService cartService;
    private final PageGenerator pageGenerator;

    @Autowired
    public ProcessUserCartController(final CartService cartService, final PageGenerator pageGenerator){
        this.cartService = cartService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = {"/userCart", "/userCart/*"}, method = RequestMethod.GET)
    protected void getUserCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("got user session. processing user card");
        Session session = (Session) request.getAttribute("session");
        List<Item> cart = session.getCart();
        String page;
        if (cart.size() > 0) {
            page = pageGenerator.getPage("userCart.html", Map.of("items", cart));
        } else {
            page = pageGenerator.getPage("userCartIsEmpty.html");
        }
        response.getWriter().write(page);
    }

    @RequestMapping(path = {"/userCart", "/userCart/*"}, method = RequestMethod.POST)
    protected void addToUserCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String stringId = uri.substring(10);
        long productId = Long.parseLong(stringId);

        Session session = (Session) request.getAttribute("session");
        List<Item> cart = session.getCart();
        cartService.addToCart(cart, productId);
        log.info("item with id " + productId + " has been added to the card");
        response.sendRedirect("/items");
    }
}
