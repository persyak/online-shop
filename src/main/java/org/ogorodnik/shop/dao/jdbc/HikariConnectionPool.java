package org.ogorodnik.shop.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;

//TODO: I do not fully understand why can't I create Singleton like it was done in PageGenerator. When I create it,
//TODO: closes connectionPool
public class HikariConnectionPool {
    private static final HikariDataSource dataSource;

    static {
        ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("context/connectionPool.xml");
            dataSource = context.getBean("dataSource", HikariDataSource.class);
    }

    static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}