package org.ogorodnik.shop.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class SearchItemsController {

    private final ItemService itemService;
    private final PageGenerator pageGenerator;

    @Autowired
    protected SearchItemsController(final ItemService itemService, final PageGenerator pageGenerator) {
        this.itemService = itemService;
        this.pageGenerator = pageGenerator;
    }

    @GetMapping("/search")
    @ResponseBody
    protected String searchItems(@RequestParam String search) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("items", itemService.search(search));
        return pageGenerator.getPage("items.html", paramsMap);
    }
}
