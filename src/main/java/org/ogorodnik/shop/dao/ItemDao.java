package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.entity.Item;

import java.util.List;

public interface ItemDao {
    List<Item> getAll();

    void insertItem(Item item);

    void deleteItem(long id);

    void updateItem(Item item, long id);

    List<Item> search(String searchItem);

    List<Item> getCard(List<Long> idList);
}
