package org.ogorodnik.shop.web.servlet;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.templater.PageGenerator;

import java.io.IOException;

@Setter
@Slf4j
public class LogoutServlet extends HttpServlet {

    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private PageGenerator pageGenerator = ServiceLocator.getService(PageGenerator.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isLoggedOut = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    log.info("logging out user");
                    isLoggedOut = securityService.logout(cookie.getValue());
                }
            }
        }

        if (isLoggedOut) {
            log.info("user has been logged out");
            response.getWriter().write(pageGenerator.getPage("logout.html"));
        } else {
            log.info("user is not logged in so no logout made");
            response.getWriter().write(pageGenerator.getPage("notloggedin.html"));
        }
    }
}
