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
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.service.UserService;
import org.ogorodnik.shop.web.servlet.*;

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
        SecurityService securityService = new SecurityService(userService);

        //config contextHandler
        ServletContextHandler contextHandler = new ServletContextHandler();

        //config servlets
        ItemsServlet itemsServlet = new ItemsServlet();
        itemsServlet.setItemService(itemService);
        itemsServlet.setSecurityService(securityService);
        ServletHolder allItemsHandler = new ServletHolder(itemsServlet);
        contextHandler.addServlet(allItemsHandler, "/items");
        contextHandler.addServlet(allItemsHandler, "/index.html");

        AddItemServlet addItemServlet = new AddItemServlet();
        addItemServlet.setItemService(itemService);
        addItemServlet.setSecurityService(securityService);
        ServletHolder addItemHandler = new ServletHolder(addItemServlet);
        contextHandler.addServlet(addItemHandler, "/additem");

        EditItemServlet editItemServlet = new EditItemServlet();
        editItemServlet.setItemService(itemService);
        editItemServlet.setSecurityService(securityService);
        ServletHolder editItemHandler = new ServletHolder(editItemServlet);
        contextHandler.addServlet(editItemHandler, "/edititem");

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);
        ServletHolder loginHandler = new ServletHolder(loginServlet);
        contextHandler.addServlet(loginHandler, "/login");

        LogoutServlet logoutServlet = new LogoutServlet();
        logoutServlet.setSecurityService(securityService);
        ServletHolder logoutHandler = new ServletHolder(logoutServlet);
        contextHandler.addServlet(logoutHandler, "/logout");

        //config server
        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(contextHandler);
        server.start();
    }
}
