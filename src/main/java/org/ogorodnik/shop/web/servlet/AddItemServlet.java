package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Slf4j
@Component
public class AddItemServlet extends HttpServlet {

    @Autowired
    private ItemService itemService;
    @Autowired
    private PageGenerator pageGenerator;

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

        try {
            itemService.insertItem(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("item " + name + " added");
        response.sendRedirect("/items");
    }
}
