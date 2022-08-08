package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.Setter;
import lombok.SneakyThrows;
import org.ogorodnik.shop.entity.Session;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;
import java.util.Optional;

@Setter
public class LoginServlet extends HttpServlet {
    private final int COOKIE_MAX_AGE = 14400;

    private SecurityService securityService;
    private PageGenerator pageGenerator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            Cookie cookie = new Cookie("user-token", session.get().getUuid());
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            String page = pageGenerator.getPage("failedlogin.html");
            response.getWriter().write(page);
        }

    }
}
