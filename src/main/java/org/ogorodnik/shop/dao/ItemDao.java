package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.entity.Item;

import java.util.List;

public interface ItemDao {
    List<Item> getAll();

    Item addItem(Item item);

    int deleteItem(long id);

    int updateItem(Item item);

    List<Item> search(String searchItem);

    Item getItemById(long id);
}
