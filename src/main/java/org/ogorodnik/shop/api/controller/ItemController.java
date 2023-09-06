package org.ogorodnik.shop.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    protected Iterable<Item> findAll() {
        log.info("Getting all items from database");
        return itemService.findAll();
    }

    @GetMapping("/{searchCriteria}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    protected Iterable<Item> findByNameOrDescription(@PathVariable String searchCriteria) {
        return itemService.findByNameOrDescription(searchCriteria);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    protected Item addItem(@Valid @RequestBody Item item) {
        return itemService.addItem(item);
    }

    @PutMapping("/edit/{itemId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    protected Item updateItem(@Valid @RequestBody Item item, @PathVariable Long itemId) {
        return itemService.updateItem(itemId, item);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    protected Item deleteItemById(@PathVariable long id) {
        return itemService.deleteItemById(id);
    }
}
