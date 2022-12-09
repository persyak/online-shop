package org.ogorodnik.shop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Slf4j
@Controller
public class LogoutController {

    private final SecurityService securityService;
    private final PageGenerator pageGenerator;

    @Autowired
    public LogoutController(final SecurityService securityService, final PageGenerator pageGenerator){
        this.securityService = securityService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    protected void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            response.getWriter().write(pageGenerator.getPage("notLoggedIn.html"));
        }
    }
}
