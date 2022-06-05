package org.ogorodnik.shop.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcItemDaoITest {

    @Test
    public void testGetAll() throws SQLException {
        String databaseConfiguration = "configurations/databaseConfiguration.properties";

        ConnectionFactory testConnectionFactory = new ConnectionFactory(databaseConfiguration);

        JdbcItemDao jdbcItemDao = new JdbcItemDao(testConnectionFactory);
        List<Item> items = jdbcItemDao.getAll();

        assertFalse(items.isEmpty());
        for (Item item : items) {
            assertNotNull(item.getId());
            assertNotNull(item.getName());
            assertNotNull(item.getPrice());
            assertNotNull(item.getCreationDate());
        }
    }
}