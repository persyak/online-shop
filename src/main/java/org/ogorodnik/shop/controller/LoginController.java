package org.ogorodnik.shop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@PropertySource(value = {"classpath:conf/application.properties"})
public class LoginController {

    private final SecurityService securityService;
    private final PageGenerator pageGenerator;

    @Value("${session.cookie.max.age}")
    private int sessionMaxAge;

    @GetMapping("/login")
    @ResponseBody
    protected String getLoginPage() {
        log.info("redirecting to login page");
        return pageGenerator.getPage("login.html");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected String login(
            @RequestParam String name,
            @RequestParam String password,
            HttpServletResponse response) throws IOException {
        Credentials credentials = Credentials.builder()
                .userName(name)
                .password(password)
                .build();

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);
            response.addCookie(cookie);
            return "redirect:/items";
        } else {
            log.info("failing to login. There is no session for user");
            return pageGenerator.getPage("failedLogin.html");
        }
    }
}
