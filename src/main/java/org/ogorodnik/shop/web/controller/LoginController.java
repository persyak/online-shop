package org.ogorodnik.shop.web.controller;

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
@PropertySource({"classpath:conf/application.properties"})
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
    @ResponseBody
    protected String login(
            @ModelAttribute("credentials") Credentials credentials,
            HttpServletResponse response) throws IOException {

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);
            response.addCookie(cookie);
            response.sendRedirect("/items");
        }
        log.info("failing to login. There is no session for user");
        return pageGenerator.getPage("failedLogin.html");
    }
}
