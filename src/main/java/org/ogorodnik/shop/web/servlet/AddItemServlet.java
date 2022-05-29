package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.utility.Validator;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemServlet extends HttpServlet {

    private ItemService itemService;
    private List<String> sessionList;

    public AddItemServlet(List<String> sessionList) {
        this.sessionList = sessionList;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if(Validator.validateIfLoggedIn(request, sessionList)) {
            PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
            PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
            String page = pageGenerator.getPage("additem.html");
            response.getWriter().write(page);
        }else{
            response.sendRedirect("/login");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

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
            itemService.insertItem(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        paramsMap.put("name", name);
        paramsMap.put("price", price);
        paramsMap.put("creationdate", creationDate);
        paramsMap.put("description", description);

        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
        String page = pageGenerator.getPage("addeditem.html", paramsMap);
        response.getWriter().write(page);
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
