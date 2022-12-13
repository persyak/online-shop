package org.ogorodnik.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
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
public class SearchItemsController {

    private final ItemService itemService;
    private final PageGenerator pageGenerator;

    @Autowired
    protected SearchItemsController(final ItemService itemService, final PageGenerator pageGenerator) {
        this.itemService = itemService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = {"/search"}, method = RequestMethod.GET)
    @ResponseBody
    protected String searchItems(HttpServletRequest request) {
        String searchItem = request.getParameter("search");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("items", itemService.search(searchItem));
        return pageGenerator.getPage("items.html", paramsMap);
    }
}
