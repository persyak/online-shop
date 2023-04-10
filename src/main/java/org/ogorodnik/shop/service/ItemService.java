package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.repository.ItemRepository;
import org.ogorodnik.shop.entity.Item;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Iterable<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItemById(long itemId) {
        itemRepository.deleteById(itemId);
    }

    public Item updateItem(Long itemId, Item item) throws ItemNotFountException {
        Item itemDb = getItemById(itemId);
        if (Objects.nonNull(item.getName()) && !"".equalsIgnoreCase(item.getName())) {
            itemDb.setName(item.getName());
        }
        if (Objects.nonNull(item.getPrice())) {
            itemDb.setPrice(item.getPrice());
        }
        if (Objects.nonNull(item.getDescription())) {
            itemDb.setDescription(item.getDescription());
        }
        return itemRepository.save(itemDb);
    }

    public Iterable<Item> findByNameAdnDescription(String searchItem) {
        return itemRepository.findByNameAndDescriptionContaining(searchItem, searchItem);
    }

    public Item getItemById(long itemId) throws ItemNotFountException {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new ItemNotFountException("Item not available");
        }

        return item.get();
    }
}
