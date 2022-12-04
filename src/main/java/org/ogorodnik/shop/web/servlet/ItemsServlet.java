package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ItemsServlet extends HttpServlet {

    private final ItemService itemService = ServiceLocator.getService(ItemService.class);
    private final PageGenerator pageGenerator =
            ServiceLocator.getService(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        log.info("Getting all items from database");
        paramsMap.put("items", itemService.getAll());
        String page = pageGenerator.getPage("items.html", paramsMap);

        response.getWriter().write(page);
    }
}
