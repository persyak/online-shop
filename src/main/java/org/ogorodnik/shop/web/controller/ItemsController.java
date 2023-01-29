package org.ogorodnik.shop.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ItemsController {

    private final ItemService itemService;

    @Autowired
    protected ItemsController(final ItemService itemService, final PageGenerator pageGenerator) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String getAll(Model model) {
        log.info("Getting all items from database");
        model.addAttribute("items", itemService.getAll());
        return "items";
    }
}
