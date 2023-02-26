package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemsController {

    private final ItemService itemService;

    @GetMapping("/items")
    public String getAll(Model model) {
        log.info("Getting all items from database");
        model.addAttribute("items", itemService.getAll());
        return "items";
    }

    @GetMapping("/search")
    protected String searchItems(@RequestParam String search, Model model) {
        model.addAttribute("items", itemService.search(search));
        return "items";
    }

    @GetMapping("/api/v1/items")
    @ResponseBody
    protected List<Item> getAll() {
        log.info("Getting all items from database");
        return itemService.getAll();
    }

    @GetMapping("/api/v1/items/{searchCriteria}")
    @ResponseBody
    protected List<Item> searchItems(@PathVariable String searchCriteria) {
        return itemService.search(searchCriteria);
    }
}
