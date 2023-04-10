package org.ogorodnik.shop.service;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.security.SecurityService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Test
    public void testAddToCart() throws ItemNotFountException {
        List<Item> testCart = new ArrayList<>();
        Item item = Item.builder().build();
        long testItemId = 5;
        ItemService mockItemService = mock(ItemService.class);
        SecurityService securityService = mock(SecurityService.class);
        when(mockItemService.getItemById(anyLong())).thenReturn(item);
        CartService testCartService = new CartService(mockItemService, securityService);
        testCartService.addToCart(testCart, testItemId);
        assert (testCart.size() == 1);
        assert (testCart.contains(item));
    }
}