package org.ogorodnik.shop.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ogorodnik.shop.utility.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@PropertySource("classpath:conf/application.properties")
@ComponentScan({"org.ogorodnik.shop.dao",
        "org.ogorodnik.shop.security",
        "org.ogorodnik.shop.service"})
public class RootConfiguration {

    @Value("${jdbc.hikari.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${jdbc.hikari.idleTimeout}")
    private long idleTimeOut;

//    @Value("${jdbc.dataSourceClassName}")
//    private String dataSourceClassName;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String userName;

    @Value("${jdbc.password}")
    private String password;

    @Value("${web.filter.url.exclude}")
    private String excludePattern;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(maximumPoolSize);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(userName);
        config.setPassword(password);

        //TODO: clarify why doesn't this parameter work
//        config.setDataSourceClassName(dataSourceClassName);

        return new HikariDataSource(config);
    }

    @Bean
    public ApplicationConfiguration applicationConfiguration() {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.setExcludePattern(excludePattern);
        return applicationConfiguration;
    }
}
