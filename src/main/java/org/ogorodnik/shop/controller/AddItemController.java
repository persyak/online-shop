package org.ogorodnik.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Controller
public class AddItemController {

    private final ItemService itemService;
    private final PageGenerator pageGenerator;

    @Autowired
    public AddItemController(final ItemService itemService, final PageGenerator pageGenerator) {
        this.itemService = itemService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/addItem", method = RequestMethod.GET)
    @ResponseBody
    protected String getAddItemPage() {
        log.info("Accessing add item page");
        return pageGenerator.getPage("addItem.html");
    }

    @RequestMapping(path = "/addItem", method = RequestMethod.POST)
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
}
