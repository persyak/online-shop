package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.entity.Item;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private JdbcItemDao jdbcItemDao;

    public List<Item> getAll() throws SQLException, IOException {
        return jdbcItemDao.getAll();
    }

    public void insertItem(Item item) throws SQLException, IOException {
        jdbcItemDao.insertItem(item);
    }

    public void deleteItem(long id) throws SQLException, IOException {
        jdbcItemDao.deleteItem(id);
    }

    public void updateItem(Item item, long id) throws SQLException, IOException {
        jdbcItemDao.updateItem(item, id);
    }

    public void setJdbcItemDao(JdbcItemDao jdbcItemDao) {
        this.jdbcItemDao = jdbcItemDao;
    }
}
