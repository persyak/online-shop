package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Setter
@Slf4j
public class AddItemServlet extends HttpServlet {

    private ItemService itemService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
        String page = pageGenerator.getPage("additem.html");
        log.info("Accessing add item page");
        response.getWriter().write(page);
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
