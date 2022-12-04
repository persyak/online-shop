package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class LoginServlet extends HttpServlet {

    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private final PageGenerator pageGenerator = ServiceLocator.getService(PageGenerator.class);
    Properties properties = PropertiesHandler.getDefaultProperties();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("redirecting to login page");
        String page = pageGenerator.getPage("login.html");
        response.getWriter().write(page);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Optional<Session> sessionOptional = Optional.ofNullable(securityService.allowLogin(name, password));
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            //TODO: is it a good practice to throw RuntimeException in the case below?
            //TODO: I'd make default param value in case of exception and wrote error to log.
            try {
                cookie.setMaxAge(Integer.parseInt(properties.getProperty("session.cookie.max.age")));
            } catch (NumberFormatException e) {
                log.error("Invalid session.cookie.max.age number format", e);
                throw new RuntimeException("Invalid session.cookie.max.age number format", e);
            }
            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            log.info("failing to login. There is no session for user");
            String page = pageGenerator.getPage("failedLogin.html");
            response.getWriter().write(page);
        }
    }
}
