package org.ogorodnik.shop.api.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginLogoutController {

    private final SecurityService securityService;
    private int sessionMaxAge;

    @PostMapping("/api/v1/login")
    protected Cookie login(@RequestBody Credentials credentials, HttpServletRequest request,
                           HttpServletResponse response) throws AuthenticationException {

        Session session = securityService.login(credentials);
        log.info("authenticate user");
        Cookie cookie = new Cookie("userToken", session.getUserToken());
        cookie.setMaxAge(sessionMaxAge);
        response.addCookie(cookie);
        request.setAttribute("session", session);

        return cookie;
    }

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

    @Value("${session.cookie.max.age}")
    public void setSessionMaxAge(int sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
    }
}
