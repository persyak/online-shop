package org.ogorodnik.shop.dao.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.security.EncryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcUserDao implements UserDao {
    private final String GET_PASSWORD_SQL = "select password, salt from users where login = ?";

    private final DataSource dataSource;

    @Autowired
    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    public Optional<EncryptedPassword> getUserPassword(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPasswordSql = connection.prepareStatement(GET_PASSWORD_SQL)) {
            getPasswordSql.setString(1, name);
            try (ResultSet resultSet = getPasswordSql.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(EncryptedPassword.builder()
                            .password(resultSet.getString("password"))
                            .salt(resultSet.getString("salt"))
                            .build());
                }
                return Optional.empty();
            }
        }
    }
}
