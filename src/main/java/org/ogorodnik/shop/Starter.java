package org.ogorodnik.shop;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.dao.jdbc.ConnectionFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.dao.jdbc.JdbcUserDao;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.service.UserService;
import org.ogorodnik.shop.web.servlet.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Starter {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        //config dao
        ItemDao itemDao = new JdbcItemDao(connectionFactory);
        UserDao userDao = new JdbcUserDao(connectionFactory);

        //config services
        ItemService itemService = new ItemService();
        itemService.setItemDao(itemDao);
        UserService userService = new UserService();
        userService.setUserDao(userDao);

        //config contextHandler
        ServletContextHandler contextHandler = new ServletContextHandler();

        List<String> sessionList = new ArrayList<>();

        //config servlets
        ItemsServlet itemsServlet = new ItemsServlet(sessionList);
        itemsServlet.setItemService(itemService);
        ServletHolder allItemsHandler = new ServletHolder(itemsServlet);
        contextHandler.addServlet(allItemsHandler, "/items");
        contextHandler.addServlet(allItemsHandler, "/");

        AddItemServlet addItemServlet = new AddItemServlet(sessionList);
        addItemServlet.setItemService(itemService);
        ServletHolder addItemHandler = new ServletHolder(addItemServlet);
        contextHandler.addServlet(addItemHandler, "/additem");

        EditItemServlet editItemServlet = new EditItemServlet(sessionList);
        editItemServlet.setItemService(itemService);
        ServletHolder editItemHandler = new ServletHolder(editItemServlet);
        contextHandler.addServlet(editItemHandler, "/edititem");

        LoginServlet loginServlet = new LoginServlet(sessionList);
        loginServlet.setUserService(userService);
        ServletHolder loginHandler = new ServletHolder(loginServlet);
        contextHandler.addServlet(loginHandler, "/login");

        LogoutServlet logoutServlet = new LogoutServlet(sessionList);
        ServletHolder logoutHandler = new ServletHolder(logoutServlet);
        contextHandler.addServlet(logoutHandler, "/logout");

        //config server
        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(contextHandler);
        server.start();
    }
}
