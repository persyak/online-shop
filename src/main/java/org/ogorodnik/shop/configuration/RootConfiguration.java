package org.ogorodnik.shop.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ogorodnik.shop.util.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@PropertySource("classpath:conf/application.properties")
@ComponentScan({"org.ogorodnik.shop.dao",
        "org.ogorodnik.shop.security",
        "org.ogorodnik.shop.service"})
public class RootConfiguration {

    @Bean
    public DataSource dataSource(
            @Value("${jdbc.hikari.maximumPoolSize}") int maximumPoolSize,
            @Value("${jdbc.url}") String jdbcUrl,
            @Value("${jdbc.username}") String userName,
            @Value("${jdbc.password}") String password) {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(maximumPoolSize);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(userName);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ApplicationConfiguration applicationConfiguration(
            @Value("${web.filter.url.exclude}") String excludePattern) {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.setExcludePattern(excludePattern);
        return applicationConfiguration;
    }
}
