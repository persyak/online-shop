package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EditItemController {

    private final ItemService itemService;

    @GetMapping("/editItem")
    protected String getEditItemPage(
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam LocalDateTime creationDate,
            @RequestParam String description,
            @RequestParam long id,
            Model model) {

        model.addAttribute("name", StringEscapeUtils.escapeHtml4(name));
        model.addAttribute("price", StringEscapeUtils.escapeHtml4(price.replaceAll(",", "")));
        model.addAttribute("creationDate", creationDate);
        model.addAttribute("description", StringEscapeUtils.escapeHtml4(description));
        model.addAttribute("id", id);

        log.info("Editing item");
        return "editItem";
    }

    @RequestMapping(path = "/editItem", method = RequestMethod.POST)
    protected String editItem(
            @RequestParam long id,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam LocalDateTime creationDate,
            @RequestParam String description,
            Model model) {

        Item item = Item.builder()
                .name(name)
                .price(price)
                .creationDate(creationDate)
                .description(description)
                .build();

        itemService.updateItem(item, id);

        model.addAttribute("name", StringEscapeUtils.escapeHtml4(name));
        model.addAttribute("price", price);
        model.addAttribute("creationdate", creationDate);
        model.addAttribute("description", StringEscapeUtils.escapeHtml4(description));

        log.info("item {} edited", name);
        return "editedItem";
    }
}
