package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Component
public class EditItemServlet extends HttpServlet {

    @Autowired
    private ItemService itemService;
    @Autowired
    private PageGenerator pageGenerator;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        String name = request.getParameter("name");
        String rowPrice = request.getParameter("price");
        String priceWithoutComma = rowPrice.replaceAll(",", "");
        LocalDateTime creationDate = LocalDateTime.parse(request.getParameter("creationDate"));
        String description = request.getParameter("description");
        long id = Long.parseLong(request.getParameter("id"));

        paramsMap.put("name", StringEscapeUtils.escapeHtml4(name));
        paramsMap.put("price", StringEscapeUtils.escapeHtml4(priceWithoutComma));
        paramsMap.put("creationdate", creationDate);
        paramsMap.put("description", StringEscapeUtils.escapeHtml4(description));
        paramsMap.put("id", id);

        log.info("Editing item");
        String page = pageGenerator.getPage("edititem.html", paramsMap);
        response.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        LocalDateTime creationDate = LocalDateTime.parse(request.getParameter("creationday"));
        String description = request.getParameter("description");

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setCreationDate(creationDate);
        item.setDescription(description);

        try {
            itemService.updateItem(item, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        paramsMap.put("name", StringEscapeUtils.escapeHtml4(name));
        paramsMap.put("price", price);
        paramsMap.put("creationdate", creationDate);
        paramsMap.put("description", StringEscapeUtils.escapeHtml4(description));

        log.info("item " + StringEscapeUtils.escapeHtml4(name) + " edited");
        String page = pageGenerator.getPage("editeditem.html", paramsMap);
        response.getWriter().write(page);
    }
}
