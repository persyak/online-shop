package org.ogorodnik.shop.api.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final SecurityService securityService;
    private int sessionMaxAge;

    @PostMapping("/api/v1/login")
    protected Cookie login(@RequestBody Credentials credentials,
                           HttpServletResponse response) throws AuthenticationException {

        Session session = securityService.login(credentials);
        log.info("authenticate user");
        Cookie cookie = new Cookie("userToken", session.getUserToken());
        cookie.setMaxAge(sessionMaxAge);
        response.addCookie(cookie);

        return cookie;
    }

    @Value("${session.cookie.max.age}")
    public void setSessionMaxAge(int sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
    }
}
