package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.SecurityService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LogoutController {

    private final SecurityService securityService;

    @PostMapping("/api/v1/logout")
    protected String logout(@RequestParam String cookie) {
        boolean isLoggedOut = false;
        if (!cookie.isEmpty()) {
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
