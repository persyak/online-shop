package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Setter
@Slf4j
public class ItemsServlet extends HttpServlet {

    private ItemService itemService = ServiceLocator.getService(ItemService.class);
    private PageGenerator pageGenerator =
            ServiceLocator.getService(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String searchItem = request.getParameter("search");
        Map<String, Object> paramsMap = new HashMap<>();
        if (null == searchItem || searchItem.isBlank()) {
            log.info("Getting all items from database");
            paramsMap.put("items", itemService.getAll());
        } else {
            paramsMap.put("items", itemService.search(searchItem));
        }
        String page = pageGenerator.getPage("items.html", paramsMap);

        response.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String searchItem = request.getParameter("search");
        if (!StringUtils.isBlank(searchItem)) {
            log.info("Searching items");
            doGet(request, response);
        } else {
            long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));
            if (0 != id) {
                log.info("Deleting item " + request.getParameter("name"));
                itemService.deleteItem(id);
                doGet(request, response);
            } else {
                doGet(request, response);
            }
        }
    }
}
