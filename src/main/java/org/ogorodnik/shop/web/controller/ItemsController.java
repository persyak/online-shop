package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
