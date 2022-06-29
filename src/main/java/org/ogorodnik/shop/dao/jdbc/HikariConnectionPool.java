package org.ogorodnik.shop.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ogorodnik.shop.utility.PropertyHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnectionPool {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;
    private static final String databaseConfiguration = "configurations/databaseConfiguration.properties";

    private static final Properties properties = PropertyHandler.readConfigPropery(databaseConfiguration);

    static {
        config.setJdbcUrl( properties.getProperty("url") );
        config.setUsername( properties.getProperty("username") );
        config.setPassword( properties.getProperty("password") );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private HikariConnectionPool() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
