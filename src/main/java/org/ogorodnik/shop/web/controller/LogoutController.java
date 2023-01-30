package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final SecurityService securityService;

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    protected String logout(@CookieValue(name = "user-token") String cookie) {
        boolean isLoggedOut = false;
        if (! cookie.isEmpty()) {
            log.info("logging out user");
            isLoggedOut = securityService.logout(cookie);
        }

        if (isLoggedOut) {
            log.info("user has been logged out");
            return "logout";
        } else {
            log.info("user is not logged in so no logout made");
            return "notLoggedIn";
        }
    }
}
