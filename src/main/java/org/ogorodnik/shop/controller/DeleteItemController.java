package org.ogorodnik.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Slf4j
@Controller
public class DeleteItemController {

    private final ItemService itemService;

    @Autowired
    public DeleteItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    protected void deleteItem(
            @RequestParam("id") long id,
            @RequestParam("name") String name,
            HttpServletResponse response) throws IOException {
        log.info("Deleting item {}", name);
        itemService.deleteItem(id);
        response.sendRedirect("/items");
    }
}
