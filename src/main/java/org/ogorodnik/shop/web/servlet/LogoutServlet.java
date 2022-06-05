package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;
import java.util.List;

public class LogoutServlet extends HttpServlet {

    private List<String> sessionList;

    public LogoutServlet(List<String> sessionList) {
        this.sessionList = sessionList;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    if (sessionList.contains(cookie.getValue())) {
                        sessionList.remove(cookie.getValue());
                        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
                        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
                        String page = pageGenerator.getPage("logout.html");
                        response.getWriter().write(page);
                    } else {
                        PageGeneratorCreator pageGeneratorCreator = new PageGeneratorCreator();
                        PageGenerator pageGenerator = pageGeneratorCreator.getPageGenerator();
                        String page = pageGenerator.getPage("notloggedin.html");
                        response.getWriter().write(page);
                    }
                }
            }
        }
    }
}
