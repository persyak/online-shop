package org.ogorodnik.shop.service;

import lombok.AllArgsConstructor;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Component
public class ItemService {

    @Autowired
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

    public List<Item> search(String searchItem) throws SQLException {
        return itemDao.search(searchItem);
    }

    public List<Item> getCard(List<Long> card) throws SQLException {
        return itemDao.getCard(card);
    }
}
