package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcessUserCartServlet extends HttpServlet {

    private final CartService cartService = ServiceLocator.getService(CartService.class);
    private final PageGenerator pageGenerator = ServiceLocator.getService(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
