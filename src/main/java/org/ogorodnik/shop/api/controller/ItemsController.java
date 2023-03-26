package org.ogorodnik.shop.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemsController {

    private final ItemService itemService;

    @GetMapping("/api/v1/items")
    protected List<Item> getAll() {
        log.info("Getting all items from database");
        return itemService.getAll();
    }

    @GetMapping("/api/v1/items/{searchCriteria}")
    protected List<Item> searchItems(@PathVariable String searchCriteria) {
        return itemService.search(searchCriteria);
    }
}
