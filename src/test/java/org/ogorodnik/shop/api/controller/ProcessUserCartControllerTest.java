package org.ogorodnik.shop.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.exception.ItemNotFountException;

import org.ogorodnik.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessUserCartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;

    private final List<Item> cart = new ArrayList<>();
    private Item item;

    @BeforeEach
    void setUp() {

        item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        cart.add(item);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenSessionIsPresent_thenReturnUserCart() throws Exception {
        Mockito.when(cartService.getCart()).thenReturn(cart);

        mockMvc.perform(get("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(cart.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(cart.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(cart.get(0).getPrice()))
                .andExpect(jsonPath("$[0].description").value(cart.get(0).getDescription()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenSessionWithEmptyCart_thenReturnEmptyCart() throws Exception {
        Mockito.when(cartService.getCart()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testAddItemToCart() throws Exception {
        Mockito.when(cartService.addToCart(1)).thenReturn(item);
        mockMvc.perform(post("/api/v1/cart/item/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andExpect(jsonPath("$.description").value(item.getDescription()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenItemIsNotPresent_thenDoNotAddItemToCartAndThrowItemNotFoundException() throws Exception {
        Mockito.when(cartService.addToCart(2)).thenThrow(new ItemNotFountException("Item not available"));

        mockMvc.perform(post("/api/v1/cart/item/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Item not available"));
    }
}