package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.*;
import lombok.Setter;
import lombok.SneakyThrows;
import org.ogorodnik.shop.service.UserService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Setter
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private List<String> sessionList;

    public LoginServlet(List<String> sessionList) {
        this.sessionList = sessionList;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
        String page = pageGenerator.getPage("login.html");
        response.getWriter().write(page);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String passwordFromDatabase = userService.getUserPassword(name);
        if (passwordFromDatabase != null && passwordFromDatabase.equals(password)) {
            String uuid = UUID.randomUUID().toString();
            sessionList.add(uuid);
            Cookie cookie = new Cookie("user-token", uuid);
            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
            PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
            String page = pageGenerator.getPage("failedlogin.html");

            response.getWriter().write(page);
        }
    }
}
