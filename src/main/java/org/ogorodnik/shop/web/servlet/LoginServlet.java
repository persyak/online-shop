package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.Setter;
import lombok.SneakyThrows;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;

@Setter
public class LoginServlet extends HttpServlet {

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

        String uuid = securityService.allowLogin(name, password);
        if (null != uuid) {
            Cookie cookie = new Cookie("user-token", uuid);
            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            String page = pageGenerator.getPage("failedlogin.html");
            response.getWriter().write(page);
        }
    }
}
