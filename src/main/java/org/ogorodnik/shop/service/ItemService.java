package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDao itemDao;

    public List<Item> getAll() {
        return itemDao.getAll();
    }

    public Item addItem(Item item) {
        return itemDao.addItem(item);
    }

    public int deleteItem(long id) {
        return itemDao.deleteItem(id);
    }

    public Item updateItem(Item item) {
        itemDao.updateItem(item);
        return item;
    }

    public List<Item> search(String searchItem) {
        return itemDao.search(searchItem);
    }

    public Item getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }
}
