package org.ogorodnik.shop.dao.jdbc;

import lombok.SneakyThrows;
import org.ogorodnik.shop.utility.PropertyHandler;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionFactory implements DataSource {
    private final String url;
    private final String user;
    private final String password;

    public ConnectionFactory(String databaseConfiguration) {
        Properties properties = PropertyHandler.readConfigPropery(databaseConfiguration);
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection(String username, String password) {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {

    }

    @Override
    public void setLoginTimeout(int seconds) {

    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
