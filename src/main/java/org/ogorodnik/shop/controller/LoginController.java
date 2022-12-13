package org.ogorodnik.shop.controller;

import jakarta.servlet.http.Cookie;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class LoginController {

    //TODO: it bother me but I'm still thinking how to improve
    private final int sessionMaxAge =
            Integer.parseInt(PropertiesHandler.getDefaultProperties().getProperty("session.cookie.max.age"));

    private final SecurityService securityService;
    private final PageGenerator pageGenerator;

    @Autowired
    public LoginController(final SecurityService securityService, final PageGenerator pageGenerator){
        this.securityService = securityService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    @ResponseBody
    protected String getLoginPage() {
        log.info("redirecting to login page");
        return pageGenerator.getPage("login.html");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected void login(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
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
            response.sendRedirect("/items");
        } else {
            log.info("failing to login. There is no session for user");
            String page = pageGenerator.getPage("failedLogin.html");
            response.getWriter().write(page);
        }
    }
}
