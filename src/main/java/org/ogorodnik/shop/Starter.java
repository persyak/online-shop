package org.ogorodnik.shop;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.ConnectionFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.servlet.AddItemServlet;
import org.ogorodnik.shop.web.servlet.EditItemServlet;
import org.ogorodnik.shop.web.servlet.ItemsServlet;
import org.ogorodnik.shop.web.servlet.LoginServlet;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Starter {

    public static void main(String[] args) throws Exception {
        String databaseConfiguration = "configurations/databaseConfiguration.properties";

        ConnectionFactory connectionFactory = new ConnectionFactory(databaseConfiguration);

        //config dao
        ItemDao itemDao = new JdbcItemDao(connectionFactory);

        //config services
        ItemService itemService = new ItemService();
        itemService.setItemDao(itemDao);

        //config contextHandler
        ServletContextHandler contextHandler = new ServletContextHandler();

        List<String> sessionList = new ArrayList<>();

        //config servlets
        ItemsServlet itemsServlet = new ItemsServlet(sessionList);
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

        LoginServlet loginServlet = new LoginServlet(sessionList);
        ServletHolder loginHandler = new ServletHolder(loginServlet);
        contextHandler.addServlet(loginHandler, "/login");

        //config server
        Server server = new Server(3000);
        server.setHandler(contextHandler);
        server.start();
    }
}
