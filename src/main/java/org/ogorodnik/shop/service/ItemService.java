package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.entity.Item;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private ItemDao itemDao;

    public List<Item> getAll() throws SQLException {
        return itemDao.getAll();
    }

    public void insertItem(Item item) throws SQLException {
        itemDao.insertItem(item);
    }

    public void deleteItem(long id) throws SQLException {
        itemDao.deleteItem(id);
    }

    public void updateItem(Item item, long id) throws SQLException {
        itemDao.updateItem(item, id);
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }
}
