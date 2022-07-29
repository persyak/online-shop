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
import org.ogorodnik.shop.web.security.PasswordManager;
import org.ogorodnik.shop.web.security.SecurityFilter;
import org.ogorodnik.shop.web.servlet.*;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

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

        //insert password with random salt into database (password == login)
        PasswordManager passwordManager = new PasswordManager(userService);
        passwordManager.setPasswordAndSalt("atrubin");
        passwordManager.setPasswordAndSalt("oohorodnik");

        //config contextHandler
        log.info("Configuring contextHandler");
        ServletContextHandler contextHandler = new ServletContextHandler();

        //create pageGenerator
        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();

        //config servlets
        log.info("Configuring servlets");
        ItemsServlet itemsServlet = new ItemsServlet();
        itemsServlet.setItemService(itemService);
        itemsServlet.setPageGenerator(pageGenerator);
        ServletHolder allItemsHandler = new ServletHolder(itemsServlet);
        contextHandler.addServlet(allItemsHandler, "/items");
        contextHandler.addServlet(allItemsHandler, "/index.html");

        AddItemServlet addItemServlet = new AddItemServlet();
        addItemServlet.setItemService(itemService);
        addItemServlet.setPageGenerator(pageGenerator);
        ServletHolder addItemHandler = new ServletHolder(addItemServlet);
        contextHandler.addServlet(addItemHandler, "/additem");

        EditItemServlet editItemServlet = new EditItemServlet();
        editItemServlet.setItemService(itemService);
        editItemServlet.setPageGenerator(pageGenerator);
        ServletHolder editItemHandler = new ServletHolder(editItemServlet);
        contextHandler.addServlet(editItemHandler, "/edititem");

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);
        loginServlet.setPageGenerator(pageGenerator);
        ServletHolder loginHandler = new ServletHolder(loginServlet);
        contextHandler.addServlet(loginHandler, "/login");

        LogoutServlet logoutServlet = new LogoutServlet();
        logoutServlet.setSecurityService(securityService);
        logoutServlet.setPageGenerator(pageGenerator);
        ServletHolder logoutHandler = new ServletHolder(logoutServlet);
        contextHandler.addServlet(logoutHandler, "/logout");

        //filters
        log.info("Configuring filter");
        SecurityFilter securityFilter = new SecurityFilter(securityService);
        contextHandler.addFilter(new FilterHolder(securityFilter), "/*", EnumSet.of(DispatcherType.REQUEST));

        //config server
        log.info("Starting server");
        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(contextHandler);
        server.start();
    }
}
