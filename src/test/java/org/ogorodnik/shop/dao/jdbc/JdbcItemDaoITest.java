package org.ogorodnik.shop.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.utility.PropertiesHandler;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcItemDaoITest {

    @Test
    public void testGetAll() {

        DataSource testDataSource = HikariDataSourceFactory.create(PropertiesHandler.getDefaultProperties());

        JdbcItemDao jdbcItemDao = new JdbcItemDao(testDataSource);
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