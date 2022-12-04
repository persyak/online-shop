package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchItemsServlet extends HttpServlet {

    private final ItemService itemService = ServiceLocator.getService(ItemService.class);
    private final PageGenerator pageGenerator =
            ServiceLocator.getService(PageGenerator.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String searchItem = request.getParameter("search");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("items", itemService.search(searchItem));
        String page = pageGenerator.getPage("items.html", paramsMap);
        response.getWriter().write(page);
    }
}
