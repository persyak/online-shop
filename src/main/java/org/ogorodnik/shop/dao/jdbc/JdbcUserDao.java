package org.ogorodnik.shop.dao.jdbc;

import org.ogorodnik.shop.dao.UserDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {
    private final String GET_PASSWORD_SQL = "select password, salt from users where login = ?";
    private final String UPDATE_PASSWORD_AND_SALT = "UPDATE users SET password=?, salt=? WHERE login=?";

    private final DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getUserPassword(String name) throws SQLException {
        List<String> credentialsList = new ArrayList<>(2);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPasswordSql = connection.prepareStatement(GET_PASSWORD_SQL)) {
            getPasswordSql.setString(1, name);
            ResultSet resultSet = getPasswordSql.executeQuery();

            if (resultSet.next()) {
                credentialsList.add(resultSet.getString("password"));
                credentialsList.add(resultSet.getString("salt"));
            }
        }
        return credentialsList;
    }

    @Override
    public void updatePasswordAndSalt(String encryptedPassword, String salt, String login) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement updatePasswordAndSalt = connection.prepareStatement(UPDATE_PASSWORD_AND_SALT)) {
            updatePasswordAndSalt.setString(1, encryptedPassword);
            updatePasswordAndSalt.setString(2, salt);
            updatePasswordAndSalt.setString(3, login);
            updatePasswordAndSalt.executeUpdate();
        }
    }
}
