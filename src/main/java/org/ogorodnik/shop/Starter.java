package org.ogorodnik.shop;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.servlet.AddItemServlet;
import org.ogorodnik.shop.web.servlet.EditItemServlet;
import org.ogorodnik.shop.web.servlet.ItemsServlet;

public class Starter {

    public static void main(String[] args) throws Exception {
        //config dao
        JdbcItemDao jdbcItemDao = new JdbcItemDao();

        //config services
        ItemService itemService = new ItemService();
        itemService.setJdbcItemDao(jdbcItemDao);

        //config contextHandler
        ServletContextHandler contextHandler = new ServletContextHandler();

        //config servlets
        ItemsServlet itemsServlet = new ItemsServlet();
        itemsServlet.setItemService(itemService);
        ServletHolder allItemsHandler = new ServletHolder(itemsServlet);
        contextHandler.addServlet(allItemsHandler, "/items");
        contextHandler.addServlet(allItemsHandler, "/");

        AddItemServlet addItemServlet = new AddItemServlet();
        addItemServlet.setItemService(itemService);
        ServletHolder addItemHandler = new ServletHolder(addItemServlet);
        contextHandler.addServlet(addItemHandler, "/additem");

        EditItemServlet editItemServlet = new EditItemServlet();
        editItemServlet.setItemService(itemService);
        ServletHolder editItemHandler = new ServletHolder(editItemServlet);
        contextHandler.addServlet(editItemHandler, "/edititem");

        //config server
        Server server = new Server(3000);
        server.setHandler(contextHandler);
        server.start();
    }
}
