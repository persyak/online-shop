package org.ogorodnik.shop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class LoginController {

    //TODO: it bother me but I'm still thinking how to improve
    int sessionMaxAge =
            Integer.parseInt(PropertiesHandler.getDefaultProperties().getProperty("session.cookie.max.age"));

    private final SecurityService securityService;
    private final PageGenerator pageGenerator;

    @Autowired
    public LoginController(SecurityService securityService, final PageGenerator pageGenerator){
        this.securityService = securityService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    protected void getLoginPage(HttpServletResponse response) throws IOException {
        log.info("redirecting to login page");
        String page = pageGenerator.getPage("login.html");
        response.getWriter().write(page);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Credentials credentials = Credentials.builder()
                .userName(request.getParameter("name"))
                .password(request.getParameter("password"))
                .build();

        Optional<Session> sessionOptional = securityService.login(credentials);
        if (sessionOptional.isPresent()) {
            log.info("login user and redirect to main page");
            Cookie cookie = new Cookie("user-token", sessionOptional.get().getUserToken());

            cookie.setMaxAge(sessionMaxAge);

            response.addCookie(cookie);
            response.sendRedirect("/items");
        } else {
            log.info("failing to login. There is no session for user");
            String page = pageGenerator.getPage("failedLogin.html");
            response.getWriter().write(page);
        }
    }
}
