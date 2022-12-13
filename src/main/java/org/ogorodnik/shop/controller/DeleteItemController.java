package org.ogorodnik.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Slf4j
@Controller
public class DeleteItemController {

    private final ItemService itemService;

    @Autowired
    public DeleteItemController(final ItemService itemService){
        this.itemService = itemService;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    protected void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));
        if (0 != id) {
            log.info("Deleting item {}", request.getParameter("name"));
            itemService.deleteItem(id);
        }
        response.sendRedirect("/items");
    }
}
