package org.ogorodnik.shop.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.utility.PropertiesHandler;
import org.ogorodnik.shop.web.util.WebUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SecurityFilter implements Filter {

    WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

    String excludePattern =
            PropertiesHandler.getDefaultProperties().getProperty("web.filter.url.exclude");
    private List<String> excludedUrls;

    private final SecurityService securityService;

    {
        assert context != null;
        securityService = context.getBean("securityService", SecurityService.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        excludedUrls = Arrays.asList(excludePattern.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String path = (httpServletRequest).getServletPath();

        if (excludedUrls.contains(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info("Check if user is authorised");

        Optional<String> tokenOptional = WebUtil.extractCookieValue(httpServletRequest, "user-token");
        if (tokenOptional.isEmpty()) {
            log.info("Unauthorised access");
            httpServletResponse.sendRedirect("/login");
            return;
        }

        Optional<Session> sessionOptional = securityService.getSession(tokenOptional.get());
        if (sessionOptional.isEmpty()) {
            log.info("Unauthorised access");
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.setAttribute("session", sessionOptional.get());
            log.info("Authorised access");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
