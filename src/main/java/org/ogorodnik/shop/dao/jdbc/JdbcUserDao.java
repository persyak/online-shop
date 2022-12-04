package org.ogorodnik.shop.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.security.EncryptedPassword;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.ogorodnik.shop.dao.jdbc.util.JdbcUtil.handleSqlException;

@Slf4j
public class JdbcUserDao implements UserDao {
    private final String GET_PASSWORD_SQL = "select password, salt from users where login = ?";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public EncryptedPassword getUserPassword(String name) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPasswordSql = connection.prepareStatement(GET_PASSWORD_SQL)) {
            getPasswordSql.setString(1, name);
            ResultSet resultSet = getPasswordSql.executeQuery();

            if (resultSet.next()) {
                return EncryptedPassword.builder()
                        .password(resultSet.getString("password"))
                        .salt(resultSet.getString("salt"))
                        .build();
            }
        } catch (SQLException throwable) {
            handleSqlException(throwable, log);
        }
        return null;
    }
}
