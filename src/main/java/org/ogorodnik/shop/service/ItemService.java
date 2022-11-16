package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.entity.Item;

import java.util.List;

@RequiredArgsConstructor
public class ItemService {
    private final ItemDao itemDao;

    public List<Item> getAll() {
        return itemDao.getAll();
    }

    public void insertItem(Item item) {
        itemDao.insertItem(item);
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

    public List<Item> getCard(List<Long> card) {
        return itemDao.getCard(card);
    }
}
