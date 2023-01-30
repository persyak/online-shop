package org.ogorodnik.shop.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@PropertySource({"classpath:conf/application.properties"})
public class LoginController {

    private final SecurityService securityService;

    @Value("${session.cookie.max.age}")
    private int sessionMaxAge;

    @GetMapping("/login")
    protected String getLoginPage() {
        log.info("redirecting to login page");
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected String login(
            @ModelAttribute("credentials") Credentials credentials,
            HttpServletResponse response) throws IOException {

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);
            response.addCookie(cookie);
            return "redirect:/items";
        }
        log.info("failing to login. There is no session for user");
        return "failedLogin";
    }
}
