package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class LoginServlet extends HttpServlet {

    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private final PageGenerator pageGenerator = ServiceLocator.getService(PageGenerator.class);
    int sessionMaxAge =
            Integer.parseInt(PropertiesHandler.getDefaultProperties().getProperty("session.cookie.max.age"));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("redirecting to login page");
        String page = pageGenerator.getPage("login.html");
        response.getWriter().write(page);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Credentials credentials = Credentials.builder()
                .userName(request.getParameter("name"))
                .password(request.getParameter("password"))
                .build();

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);

            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            log.info("failing to login. There is no session for user");
            String page = pageGenerator.getPage("failedLogin.html");
            response.getWriter().write(page);
        }
    }
}
