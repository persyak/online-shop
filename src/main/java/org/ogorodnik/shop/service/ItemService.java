package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.exception.ItemNotFountException;
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

    public Item deleteItemById(long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new ItemNotFountException("Item not available");
        }
        itemRepository.deleteById(itemId);
        if (itemRepository.findById(itemId).isPresent()) {
            throw new RuntimeException("Something went wrong. Item was not deleted");
        }
        return itemOptional.get();
    }

    public Item updateItem(Long itemId, Item item) {
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

    public Iterable<Item> findByNameOrDescription(String searchItem) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchItem, searchItem);
    }

    public Item getItemById(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new ItemNotFountException("Item not available");
        }

        return item.get();
    }
}
