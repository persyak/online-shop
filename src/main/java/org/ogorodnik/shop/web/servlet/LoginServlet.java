package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LoginServlet extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uuid = UUID.randomUUID().toString();
        sessionList.add(uuid);
        Cookie cookie = new Cookie("user-token", uuid);
        response.addCookie(cookie);
        response.sendRedirect("/items");
    }
}
