package org.ogorodnik.shop.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ServiceLocator;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcItemDaoITest {

    @Test
    public void testGetAll() {

        Properties properties = ServiceLocator.getProperties();
        DataSource testDataSource = HikariDataSourceFactory.create(properties);

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