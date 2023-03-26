package org.ogorodnik.shop.api.controller;

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

    @PostMapping("/api/v1/item/add")
    protected Item addItem(@RequestBody Item item) {
        return itemService.addItem(item);
    }

    @PostMapping("/api/v1/item/edit/{id}")
    protected Item editItem(@RequestBody Item item, @PathVariable long id) {
        item.setId(id);
        return itemService.updateItem(item);
    }

    @DeleteMapping("/api/v1/item/delete/{id}")
    protected String deleteItem(@PathVariable long id) {
        int deleteCount = itemService.deleteItem(id);
        if (deleteCount > 0) {
            return "item has been deleted";
        }
        return "item has not been deleted";
    }
}
