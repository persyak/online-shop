package org.ogorodnik.shop.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    @Value("${session.cookie.max.age}")
    private int sessionMaxAge;

    @GetMapping("/login")
    protected String getLoginPage() {
        log.info("redirecting to login page");
        return "login";
    }

    @PostMapping ("/login")
    protected String login(@ModelAttribute("credentials") Credentials credentials,
                           HttpServletResponse response) {

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("userToken", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);
            response.addCookie(cookie);
            return "redirect:/items";
        }
        log.info("failing to login. There is no session for user");
        return "failedLogin";
    }

    @PostMapping("/api/v1/login")
    @ResponseBody
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
