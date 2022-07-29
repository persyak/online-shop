package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Setter
@Slf4j
public class ItemsServlet extends HttpServlet {

    private ItemService itemService;
    private PageGenerator pageGenerator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String searchItem = request.getParameter("search");
        Map<String, Object> paramsMap = new HashMap<>();
        if (null == searchItem || searchItem.isBlank()) {
            log.info("Getting all items from database");
            try {
                paramsMap.put("items", itemService.getAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                paramsMap.put("items", itemService.search(searchItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String page = pageGenerator.getPage("items.html", paramsMap);

        response.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String searchItem = request.getParameter("search");
        if (!(null == searchItem) && !searchItem.isBlank()) {
            log.info("Searching items from database");
            doGet(request, response);
        } else {
            long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));
            if (0 != id) {
                log.info("Deleting item " + request.getParameter("name"));
                try {
                    itemService.deleteItem(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                doGet(request, response);
            } else {
                doGet(request, response);
            }
        }
    }
}
