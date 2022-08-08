package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;

@Setter
public class LogoutServlet extends HttpServlet {

    private SecurityService securityService;
    private PageGenerator pageGenerator;

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
            response.getWriter().write(pageGenerator.getPage("logout.html"));
        } else {
            response.getWriter().write(pageGenerator.getPage("notloggedin.html"));
        }
    }
}
