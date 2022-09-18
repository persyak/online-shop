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
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.EnumSet;

@Slf4j
public class Starter {

    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext context =
                 new ClassPathXmlApplicationContext("context/applicationContext.xml")) {

            //config dao
            log.info("Configuring dao");

            //config services
            log.info("Configuring services");
            ItemService itemService = context.getBean("itemService", ItemService.class);
            SecurityService securityService = context.getBean("securityService", SecurityService.class);

            //insert password with random salt into database (password == login)
            PasswordManager passwordManager = context.getBean("passwordManager", PasswordManager.class);
            passwordManager.setPasswordAndSalt("atrubin");
            passwordManager.setPasswordAndSalt("oohorodnik");

            //config contextHandler
            log.info("Configuring contextHandler");
            ServletContextHandler contextHandler = context.getBean("contextHandler", ServletContextHandler.class);

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

            ProcessUserCardServlet processUserCardServlet = new ProcessUserCardServlet();
            processUserCardServlet.setSecurityService(securityService);
            processUserCardServlet.setPageGenerator(pageGenerator);
            ServletHolder processUserCardHandler = new ServletHolder(processUserCardServlet);
            contextHandler.addServlet(processUserCardHandler, "/usercard");
            contextHandler.addServlet(processUserCardHandler, "/usercard/*");
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
}
