package org.ogorodnik.shop.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@PropertySource("classpath:conf/application.properties")
public class RootConfiguration {

    @Value("${jdbc.hikari.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${jdbc.hikari.idleTimeout}")
    private long idleTimeOut;

    @Value("${jdbc.dataSourceClassName}")
    private String dataSourceClassName;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String userName;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(maximumPoolSize);
        config.setDataSourceClassName(dataSourceClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(userName);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

//    @Bean
//    public HikariConfig hikariConfig(){
//
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setPoolName("springHikariCP");
//        hikariConfig.setConnectionTestQuery("SELECT 1");
//        hikariConfig.setDataSourceClassName(dataSourceClassName);
//        hikariConfig.setMaximumPoolSize(maximumPoolSize);
//        hikariConfig.setIdleTimeout(idleTimeOut);
//
//        hikariConfig.setJdbcUrl(jdbcUrl);
//        hikariConfig.setUsername(userName);
//        hikariConfig.setPassword(password);
//
//        return hikariConfig;
//    }
//
//    @Bean
//    public DataSource dataSource(){
//        return new HikariDataSource(hikariConfig());
//    }

    @Bean
    public ItemDao itemDao(){
        return new JdbcItemDao(dataSource());
    }

    @Bean
    public ItemService itemService(){
        return new ItemService(itemDao());
    }

}
