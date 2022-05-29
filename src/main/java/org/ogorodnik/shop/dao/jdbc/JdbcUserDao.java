package org.ogorodnik.shop.dao.jdbc;

import org.ogorodnik.shop.dao.UserDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private final String GET_PASSWORD_SQL = "select password from users where login = ?";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getUserPassword(String name) throws SQLException {
        String password = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPasswordSql = connection.prepareStatement(GET_PASSWORD_SQL)) {
            getPasswordSql.setString(1, name);
            ResultSet resultSet = getPasswordSql.executeQuery();
            if (resultSet.next()) {
                password = resultSet.getString("password");
            }
        }
        return password;
    }
}
