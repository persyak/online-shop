package org.ogorodnik.shop.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SecurityFilter implements Filter {

    @Value("${web.filter.url.exclude}")
    private String excludePattern;

    private final SecurityService securityService;
    private List<String> excludedUrls;

    @Autowired
    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
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
        if (!excludedUrls.contains(path)) {
            log.info("Check if user is authorised");
            Cookie[] cookies = httpServletRequest.getCookies();
            if (null == cookies) {
                log.info("Unauthorised access");
                httpServletResponse.sendRedirect("/login");
            } else {
                for (Cookie cookie : cookies) {
                    if ("user-token".equals(cookie.getName())) {
                        if (!securityService.validateIfLoggedIn(cookie.getValue())) {
                            log.info("Unauthorised access");
                            httpServletResponse.sendRedirect("/login");
                        } else {
                            log.info("Authorised access");
                            filterChain.doFilter(servletRequest, servletResponse);
                        }
                    }
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
