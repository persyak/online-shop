package org.ogorodnik.shop.configuration;

import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("org.ogorodnik.shop")
@EnableWebMvc
public class SpringConfiguration {

//    private final ApplicationContext applicationContext;
//
//    @Autowired
//    public SpringConfiguration(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
//
//    @Bean
//    public PageGenerator pageGenerator(){
//        return new PageGenerator();
//    }
}
