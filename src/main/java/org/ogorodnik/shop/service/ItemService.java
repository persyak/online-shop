package org.ogorodnik.shop.service;

import org.ogorodnik.shop.entity.Item;

public interface ItemService {
    Iterable<Item> findAll();

    Item addItem(Item item);

    Item deleteItemById(long itemId);

    Item updateItem(Long itemId, Item item);

    Iterable<Item> findByNameOrDescription(String searchItem);

    Item getItemById(long itemId);
}
