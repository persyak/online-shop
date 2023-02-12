package org.ogorodnik.shop.configuration;

import jakarta.servlet.Filter;
import org.ogorodnik.shop.web.security.SecurityFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class SpringMvcDispatcherServletInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfiguration.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new SecurityFilter()};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
