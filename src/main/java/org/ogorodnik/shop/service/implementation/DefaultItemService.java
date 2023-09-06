package org.ogorodnik.shop.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.exception.ItemNotFountException;
import org.ogorodnik.shop.repository.ItemRepository;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Iterable<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
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

    @Override
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

    @Override
    public Iterable<Item> findByNameOrDescription(String searchItem) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchItem, searchItem);
    }

    @Override
    public Item getItemById(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new ItemNotFountException("Item not available");
        }

        return item.get();
    }
}
