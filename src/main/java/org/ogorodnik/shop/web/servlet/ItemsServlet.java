package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsServlet extends HttpServlet {

    private ItemService itemService;
    private List<String> sessionList;

    public ItemsServlet(List<String> sessionList) {
        this.sessionList = sessionList;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        boolean isValid = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    if (sessionList.contains(cookie.getValue())) {
                        isValid = true;
                    }
                    break;
                }
            }
        }

        if (!isValid) {
            response.sendRedirect("/login");
        }

        Map<String, Object> paramsMap = new HashMap<>();
        try {
            paramsMap.put("items", itemService.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
        String page = pageGenerator.getPage("items.html", paramsMap);

        response.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));

        try {
            itemService.deleteItem(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        doGet(request, response);
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
