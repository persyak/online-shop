package org.ogorodnik.shop.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/api/v1/items")
    protected Iterable<Item> findAll() {
        log.info("Getting all items from database");
        return itemService.findAll();
    }

    @GetMapping("/api/v1/items/{searchCriteria}")
    protected Iterable<Item> findByNameOrDescription(@PathVariable String searchCriteria) {
        return itemService.findByNameOrDescription(searchCriteria);
    }

    @PostMapping("/api/v1/item/add")
    protected Item addItem(@Valid @RequestBody Item item) {
        return itemService.addItem(item);
    }

    @PutMapping("/api/v1/item/edit/{itemId}")
    protected Item updateItem(@Valid @RequestBody Item item, @PathVariable Long itemId) {
        return itemService.updateItem(itemId, item);
    }

    @DeleteMapping("/api/v1/item/delete/{id}")
    protected String deleteItemById(@PathVariable long id) {
        itemService.deleteItemById(id);
        return "item has been deleted";
    }
}
