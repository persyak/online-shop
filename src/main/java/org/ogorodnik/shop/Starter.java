package org.ogorodnik.shop;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.ogorodnik.shop.web.security.PasswordManager;
import org.ogorodnik.shop.web.security.SecurityFilter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.EnumSet;

@Slf4j
public class Starter {

    public static void main(String[] args) throws Exception {
        try (ClassPathXmlApplicationContext context =
                 new ClassPathXmlApplicationContext("context/applicationContext.xml")) {

            //insert password with random salt into database (password == login)
            PasswordManager passwordManager = context.getBean("passwordManager", PasswordManager.class);
            passwordManager.setPasswordAndSalt("atrubin");
            passwordManager.setPasswordAndSalt("oohorodnik");

            //config contextHandler
            log.info("Configuring contextHandler");
            ServletContextHandler contextHandler = context.getBean("contextHandler", ServletContextHandler.class);

            //config servlets
            log.info("Configuring servlets");
            ServletHolder allItemsHandler = context.getBean("allItemsHandler", ServletHolder.class);
            ServletHolder addItemHandler = context.getBean("addItemHandler", ServletHolder.class);
            ServletHolder editItemHandler = context.getBean("editItemHandler", ServletHolder.class);
            ServletHolder processUserCardHandler = context.getBean("processUserCardHandler", ServletHolder.class);
            ServletHolder loginHandler = context.getBean("loginHandler", ServletHolder.class);
            ServletHolder logoutHandler = context.getBean("logoutHandler", ServletHolder.class);

            contextHandler.addServlet(allItemsHandler, "/items");
            contextHandler.addServlet(addItemHandler, "/additem");
            contextHandler.addServlet(editItemHandler, "/edititem");
            contextHandler.addServlet(processUserCardHandler, "/usercard");
            contextHandler.addServlet(processUserCardHandler, "/usercard/*");
            contextHandler.addServlet(loginHandler, "/login");
            contextHandler.addServlet(logoutHandler, "/logout");

            //filters
            log.info("Configuring filter");
            SecurityFilter securityFilter = context.getBean("securityFilter", SecurityFilter.class);
            contextHandler.addFilter(new FilterHolder(securityFilter), "/*", EnumSet.of(DispatcherType.REQUEST));

            //config server
            log.info("Starting server");
            Server server = context.getBean("server", Server.class);
            server.setHandler(contextHandler);
            server.start();
        }
    }
}
