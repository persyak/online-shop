package org.ogorodnik.shop.dao.jdbc.mapper;

import org.ogorodnik.shop.security.Credentials;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class CredentialsRowMapper implements RowMapper<Credentials> {
    @Override
    public Credentials mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Credentials.builder()
                .login(resultSet.getString("login"))
                .password(resultSet.getString("password"))
                .salt(resultSet.getString("salt"))
                .build();
    }
}
