package org.ogorodnik.shop.web.controller;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    @Value("${session.cookie.max.age}")
    private int sessionMaxAge;

    @PostMapping("/api/v1/login")
    protected Cookie login(@RequestBody Credentials credentials) throws AuthenticationException {

        Optional<Session> sessionOptional = securityService.login(credentials);
        //TODO: have to think about this exception if login fails
        if (sessionOptional.isEmpty()) {
            throw new AuthenticationException("User or password is not correct");
        }
        log.info("login user and redirect to main page");
        Cookie cookie = new Cookie("userToken", sessionOptional.get().getUserToken());
        cookie.setMaxAge(sessionMaxAge);

        return cookie;
    }
}
