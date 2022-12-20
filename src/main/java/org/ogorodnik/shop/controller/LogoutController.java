package org.ogorodnik.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class LogoutController {

    private final SecurityService securityService;
    private final PageGenerator pageGenerator;

    @Autowired
    public LogoutController(final SecurityService securityService, final PageGenerator pageGenerator) {
        this.securityService = securityService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    protected String logout(@CookieValue(name = "user-token") String cookie) {
        boolean isLoggedOut = false;
        if (! cookie.isEmpty()) {
            log.info("logging out user");
            isLoggedOut = securityService.logout(cookie);
        }

        if (isLoggedOut) {
            log.info("user has been logged out");
            return pageGenerator.getPage("logout.html");
        } else {
            log.info("user is not logged in so no logout made");
            return pageGenerator.getPage("notLoggedIn.html");
        }
    }
}
