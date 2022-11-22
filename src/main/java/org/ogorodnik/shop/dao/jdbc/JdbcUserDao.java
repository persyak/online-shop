package org.ogorodnik.shop.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.UserDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.ogorodnik.shop.dao.jdbc.util.JdbcUtil.handleSqlException;

@Slf4j
public class JdbcUserDao implements UserDao {
    private final String GET_PASSWORD_SQL = "select password, salt from users where login = ?";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getUserPassword(String name) {
        List<String> credentialsList = new ArrayList<>(2);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPasswordSql = connection.prepareStatement(GET_PASSWORD_SQL)) {
            getPasswordSql.setString(1, name);
            ResultSet resultSet = getPasswordSql.executeQuery();

            if (resultSet.next()) {
                credentialsList.add(resultSet.getString("password"));
                credentialsList.add(resultSet.getString("salt"));
            }
        } catch (SQLException throwable) {
            handleSqlException(throwable, log);
        }
        return credentialsList;
    }
}
