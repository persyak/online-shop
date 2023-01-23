package org.ogorodnik.shop.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("org.ogorodnik.shop.web")
@EnableWebMvc
public class SpringConfiguration {

}
