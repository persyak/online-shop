package org.ogorodnik.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class DeleteItemController {

    private final ItemService itemService;

    @Autowired
    public DeleteItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    protected String deleteItem(
            @RequestParam("id") long id,
            @RequestParam("name") String name) {
        log.info("Deleting item {}", name);
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}
