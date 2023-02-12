package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.entity.Item;

import java.util.List;

public interface ItemDao {
    List<Item> getAll();

    void addItem(Item item);

    void deleteItem(long id);

    int updateItem(Item item);

    List<Item> search(String searchItem);

    Item getItemById(long id);
}
