package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.ServiceLocator;

import java.io.IOException;

@Slf4j
public class DeleteItemServlet extends HttpServlet {

    private final ItemService itemService = ServiceLocator.getService(ItemService.class);

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));
        if (0 != id) {
            log.info("Deleting item {}", request.getParameter("name"));
            itemService.deleteItem(id);
        }
        response.sendRedirect("/items");
    }
}
