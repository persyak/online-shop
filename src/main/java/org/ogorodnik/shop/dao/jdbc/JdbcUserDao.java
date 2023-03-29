package org.ogorodnik.shop.dao.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.dao.jdbc.mapper.CredentialsRowMapper;
import org.ogorodnik.shop.security.Credentials;
import org.ogorodnik.shop.security.EncryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final CredentialsRowMapper credentialsRowMapper;
    private final String GET_PASSWORD_SQL = "select login, password, salt from users where login = ?";

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate, CredentialsRowMapper credentialsRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.credentialsRowMapper = credentialsRowMapper;
    }

    @SneakyThrows
    public Optional<EncryptedPassword> getUserPassword(String login) {
        Credentials credentials = jdbcTemplate.queryForObject(GET_PASSWORD_SQL,
                credentialsRowMapper, login);
        return Optional.ofNullable(EncryptedPassword.builder()
                .password(credentials.getPassword())
                .salt(credentials.getSalt())
                .build());
    }
}
