package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item/add")
    protected String getAddItemPage() {
        log.info("Accessing add item page");
        return "addItem";
    }

    @RequestMapping(path = "/item/add", method = RequestMethod.POST)
    protected String addItem(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam String description) {

        Item item = Item.builder()
                .name(name)
                .price(price)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description(description)
                .build();

        itemService.addItem(item);
        log.info("item {} added", name);
        return "redirect:/items";
    }

    @GetMapping("/item/edit")
    protected String getEditItemPage(@ModelAttribute Item item, Model model) {

        model.addAttribute("name", item.getName());
        model.addAttribute("price", item.getPrice());
        model.addAttribute("creationDate", item.getCreationDate());
        model.addAttribute("description", item.getDescription());
        model.addAttribute("id", item.getId());

        log.info("Editing item");
        return "editItem";
    }

    @RequestMapping(path = "/item/edit", method = RequestMethod.POST)
    protected String editItem(
            @ModelAttribute Item item,
            Model model) {

        Item updatedItem = itemService.updateItem(item);

        model.addAttribute("name", updatedItem.getName());
        model.addAttribute("price", updatedItem.getPrice());
        model.addAttribute("creationdate", updatedItem.getCreationDate());
        model.addAttribute("description", updatedItem.getDescription());

        log.info("item {} edited", updatedItem.getName());
        return "editedItem";
    }

    @RequestMapping(path = "/item/delete", method = RequestMethod.POST)
    protected String deleteItem(
            @RequestParam long id,
            @RequestParam String name) {
        log.info("Deleting item {}", name);
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}
