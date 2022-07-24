package org.ogorodnik.shop;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
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
import org.ogorodnik.shop.web.security.SecurityFilter;
import org.ogorodnik.shop.web.servlet.*;

import java.util.EnumSet;

@Slf4j
public class Starter {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        //config dao
        log.info("Configuring dao");
        ItemDao itemDao = new JdbcItemDao(connectionFactory);
        UserDao userDao = new JdbcUserDao(connectionFactory);

        //config services
        log.info("Configuring services");
        ItemService itemService = new ItemService();
        itemService.setItemDao(itemDao);
        UserService userService = new UserService();
        userService.setUserDao(userDao);
        SecurityService securityService = new SecurityService(userService);

        //config contextHandler
        log.info("Configuring contextHandler");
        ServletContextHandler contextHandler = new ServletContextHandler();

        //filters
        log.info("Configuring filter");
        SecurityFilter securityFilter = new SecurityFilter(securityService);

        //config servlets
        log.info("Configuring servlets");
        ItemsServlet itemsServlet = new ItemsServlet();
        itemsServlet.setItemService(itemService);
        ServletHolder allItemsHandler = new ServletHolder(itemsServlet);
        contextHandler.addServlet(allItemsHandler, "/items");
        contextHandler.addServlet(allItemsHandler, "/index.html");
        contextHandler.addFilter(new FilterHolder(securityFilter), "/items", EnumSet.of(DispatcherType.REQUEST));

        AddItemServlet addItemServlet = new AddItemServlet();
        addItemServlet.setItemService(itemService);
        ServletHolder addItemHandler = new ServletHolder(addItemServlet);
        contextHandler.addServlet(addItemHandler, "/additem");
        contextHandler.addFilter(new FilterHolder(securityFilter), "/additem", EnumSet.of(DispatcherType.REQUEST));

        EditItemServlet editItemServlet = new EditItemServlet();
        editItemServlet.setItemService(itemService);
        ServletHolder editItemHandler = new ServletHolder(editItemServlet);
        contextHandler.addServlet(editItemHandler, "/edititem");
        contextHandler.addFilter(new FilterHolder(securityFilter), "/edititem", EnumSet.of(DispatcherType.REQUEST));

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);
        ServletHolder loginHandler = new ServletHolder(loginServlet);
        contextHandler.addServlet(loginHandler, "/login");

        LogoutServlet logoutServlet = new LogoutServlet();
        logoutServlet.setSecurityService(securityService);
        ServletHolder logoutHandler = new ServletHolder(logoutServlet);
        contextHandler.addServlet(logoutHandler, "/logout");

        //config server
        log.info("Starting server");
        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(contextHandler);
        server.start();
    }
}
