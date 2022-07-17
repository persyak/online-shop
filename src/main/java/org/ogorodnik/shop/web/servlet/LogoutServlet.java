package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.ogorodnik.shop.web.templater.PageGeneratorCreator;

import java.io.IOException;

@Setter
public class LogoutServlet extends HttpServlet {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean isLoggedOut = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    isLoggedOut = securityService.logout(cookie.getValue());
                }
            }
        }

        if (isLoggedOut) {
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
