package org.ogorodnik.shop.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessUserCartController.class)
class ProcessUserCartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;

    private Session session;
    private String userToken;
    List<Item> cart;

    @BeforeEach
    void setUp() {

        userToken = "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36";

        session = new Session(userToken, LocalDateTime.now());

        Item item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        cart = session.getCart();

        cart.add(item);

    }

    @Test
    public void whenSessionIsPresent_thenReturnUserCart() throws Exception {

        Mockito.when(cartService.getSession(userToken)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/api/v1/userCart")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(cart.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(cart.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(cart.get(0).getPrice()))
                .andExpect(jsonPath("$[0].description").value(cart.get(0).getDescription()));
    }

    @Test
    public void whenSessionWithEmptyCart_thenReturnEmptyCart() throws Exception {
        Session emptyCartSession = new Session(userToken, LocalDateTime.now());

        Mockito.when(cartService.getSession(userToken)).thenReturn(Optional.of(emptyCartSession));

        mockMvc.perform(get("/api/v1/userCart")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    public void whenSessionIsEmpty_thenDoNotProcessAddItemToCartAndReturnEmptyOptional() throws Exception {

        Mockito.when(cartService.getSession(userToken)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/userCart/1")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}