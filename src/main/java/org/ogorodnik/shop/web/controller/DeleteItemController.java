package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DeleteItemController {

    private final ItemService itemService;

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    protected String deleteItem(
            @RequestParam long id,
            @RequestParam String name) {
        log.info("Deleting item {}", name);
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}
