package org.ogorodnik.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class EditItemController {

    private final ItemService itemService;
    private final PageGenerator pageGenerator;

    @Autowired
    public EditItemController(final ItemService itemService, final PageGenerator pageGenerator) {
        this.itemService = itemService;
        this.pageGenerator = pageGenerator;
    }

    @RequestMapping(path = "/editItem", method = RequestMethod.GET)
    @ResponseBody
    protected String getEditItemPage(
            @RequestParam("name") String name,
            @RequestParam("price") String rowPrice,
            @RequestParam("creationDate") LocalDateTime creationDate,
            @RequestParam("description") String description,
            @RequestParam("id") long id) {
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("name", StringEscapeUtils.escapeHtml4(name));
        paramsMap.put("price", StringEscapeUtils.escapeHtml4(rowPrice.replaceAll(",", "")));
        paramsMap.put("creationDate", creationDate);
        paramsMap.put("description", StringEscapeUtils.escapeHtml4(description));
        paramsMap.put("id", id);

        log.info("Editing item");
        return pageGenerator.getPage("editItem.html", paramsMap);
    }

    @RequestMapping(path = "/editItem", method = RequestMethod.POST)
    @ResponseBody
    protected String editItem(
            @RequestParam("id") long id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("creationDate") LocalDateTime creationDate,
            @RequestParam("description") String description) {

        Map<String, Object> paramsMap = new HashMap<>();
        Item item = Item.builder()
                .name(name)
                .price(price)
                .creationDate(creationDate)
                .description(description)
                .build();

        itemService.updateItem(item, id);

        paramsMap.put("name", StringEscapeUtils.escapeHtml4(name));
        paramsMap.put("price", price);
        paramsMap.put("creationdate", creationDate);
        paramsMap.put("description", StringEscapeUtils.escapeHtml4(description));

        log.info("item {} edited", name);
        return pageGenerator.getPage("editedItem.html", paramsMap);
    }
}