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

        String searchItem = request.getParameter("search");
        Map<String, Object> paramsMap = new HashMap<>();
        if(null == searchItem || searchItem.isBlank()) {
            try {
                paramsMap.put("items", itemService.getAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else{
            try {
                paramsMap.put("items", itemService.search(searchItem));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
        String page = pageGenerator.getPage("items.html", paramsMap);

        response.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String searchItem = request.getParameter("search");
        if(!(null == searchItem) && !searchItem.isBlank()){
            doGet(request, response);
        } else {
            long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));

            if (0 != id) {
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

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
