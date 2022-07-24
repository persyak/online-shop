package org.ogorodnik.shop.web.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.SecurityService;
import org.ogorodnik.shop.utility.PropertyHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class SecurityFilter implements Filter {

    private final SecurityService securityService;
    private List<String> excludedUrls;
    private final String filtersConfiguration = "configurations/filtersConfiguration.properties";

    private final Properties properties = PropertyHandler.readConfigPropery(filtersConfiguration);

    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        String excludePattern = properties.getProperty("urlsToExludeFromFiltering");
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
