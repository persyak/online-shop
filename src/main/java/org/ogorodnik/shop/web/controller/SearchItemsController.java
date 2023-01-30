package org.ogorodnik.shop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchItemsController {

    private final ItemService itemService;

    @GetMapping("/search")
    protected String searchItems(@RequestParam String search,
                                 Model model) {
        model.addAttribute("items", itemService.search(search));
        return "items";
    }
}
