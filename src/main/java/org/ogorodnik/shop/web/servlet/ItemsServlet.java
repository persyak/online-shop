package org.ogorodnik.shop.web.servlet;

import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ItemsServlet extends HttpServlet {

    private ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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

//    private List<Item> createMockList(){
//        List<Item> itemList = new ArrayList<>();
//        Item item = new Item();
//        item.setId(1);
//        item.setName("testItemOne");
//        item.setPrice(10.6);
//        item.setCreationDate(LocalDateTime.of(1990, Month.AUGUST, 12, 11, 0));
//
//        Item item2 = new Item();
//        item2.setId(2);
//        item2.setName("testItemTwo");
//        item2.setPrice(6.0);
//        item2.setCreationDate(LocalDateTime.of(1990, Month.AUGUST, 12, 11, 0));
//
//        itemList.add(item);
//        itemList.add(item2);
//
//        return itemList;
//    }

}
