package org.ogorodnik.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.ogorodnik.shop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ItemsController {

    private final ItemService itemService;
    private final PageGenerator pageGenerator;

    @Autowired
    protected ItemsController(final ItemService itemService, final PageGenerator pageGenerator){
        this.itemService = itemService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/items", method = RequestMethod.GET)
    @ResponseBody
    public String getAll() {
        Map<String, Object> paramsMap = new HashMap<>();
        log.info("Getting all items from database");
        paramsMap.put("items", itemService.getAll());
        return pageGenerator.getPage("items.html", paramsMap);
    }
}
