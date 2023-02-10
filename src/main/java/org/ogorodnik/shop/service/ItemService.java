package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDao itemDao;

    public List<Item> getAll() {
        return itemDao.getAll();
    }

    public void addItem(Item item) {
        itemDao.addItem(item);
    }

    public void deleteItem(long id) {
        itemDao.deleteItem(id);
    }

    public void updateItem(Item item, long id) {
        itemDao.updateItem(item, id);
    }

    public List<Item> search(String searchItem) {
        return itemDao.search(searchItem);
    }

    public Item getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }
}
