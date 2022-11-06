package org.ogorodnik.shop.service;

import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.ConnectionFactory;
import org.ogorodnik.shop.dao.jdbc.JdbcItemDao;
import org.ogorodnik.shop.entity.Item;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    //TODO: it's very interesting why I can't get JdbdcItemDao from ServiceLocator.
    //Even if using class JdbdcItemDao instead of interface in ServiceLocator, it does
    //not help. I guess it's because when the object of ItemService is created,
    //it can't get access to static block
    private final ItemDao itemDao = new JdbcItemDao(ConnectionFactory.getInstance());

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

    public List<Item> search(String searchItem) throws SQLException {
        return itemDao.search(searchItem);
    }

    public List<Item> getCard(List<Long> card) throws SQLException {
        return itemDao.getCard(card);
    }

//    public void setItemDao(ItemDao itemDao) {
//        this.itemDao = itemDao;
//    }
}
