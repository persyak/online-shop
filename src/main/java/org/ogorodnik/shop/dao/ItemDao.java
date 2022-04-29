package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao {
    List<Item> getAll() throws SQLException;

    void insertItem(Item item) throws SQLException;

    void deleteItem(long id) throws SQLException;

    void updateItem(Item item, long id) throws SQLException;
}
