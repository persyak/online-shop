package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Setter
@Slf4j
public class AddItemServlet extends HttpServlet {

    private ItemService itemService = ServiceLocator.getService(ItemService.class);
    private PageGenerator pageGenerator =
            ServiceLocator.getService(PageGenerator.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Accessing add item page");
        response.getWriter().write(pageGenerator.getPage("additem.html"));
    }

    @SneakyThrows
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) {

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        String description = request.getParameter("description");

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setCreationDate(creationDate);
        item.setDescription(description);

        itemService.insertItem(item);
        log.info("item " + name + " added");
        response.sendRedirect("/items");
    }
}
