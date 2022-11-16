package org.ogorodnik.shop.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Session;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.service.ServiceLocator;
import org.ogorodnik.shop.web.util.WebUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class SecurityFilter implements Filter {

    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private List<String> excludedUrls;

    private final Properties properties = ServiceLocator.getProperties();

    @Override
    public void init(FilterConfig filterConfig) {
        String excludePattern = properties.getProperty("web.filter.url.exclude");
        excludedUrls = Arrays.asList(excludePattern.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String path = (httpServletRequest).getServletPath();

        //TODO: move to methods
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

        Session session = securityService.getSession(tokenOptional.get());
        //TODO: make session optional
        if (session == null) {
            log.info("Unauthorised access");
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.setAttribute("session", session);
            log.info("Authorised access");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
