package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Session;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Component
public class LoginServlet extends HttpServlet {
    private final int COOKIE_MAX_AGE = 14400;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private PageGenerator pageGenerator;

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

        Optional<Session> session = Optional.ofNullable(securityService.allowLogin(name, password));
        if (session.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", session.get().getUuid());
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            log.info("failing to login. There is no session for user");
            String page = pageGenerator.getPage("failedlogin.html");
            response.getWriter().write(page);
        }
    }
}
