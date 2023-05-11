package org.ogorodnik.shop.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.exception.ItemNotFountException;
import org.ogorodnik.shop.exception.SessionNotFoundException;
import org.ogorodnik.shop.security.Session;
import org.ogorodnik.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private String nonExistedToken;
    private List<Item> cart;
    private Item item;
    private Credentials credentials;

    @BeforeEach
    void setUp() {

        userToken = "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36";

        nonExistedToken = "1c0451d1-12c7-43ff-9a3f-doesNotExist";

        credentials = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .build();

        session = new Session(userToken, LocalDateTime.now(), credentials);

        item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        cart = session.getCart();

        cart.add(item);

        Mockito.when(cartService.getSession(userToken)).thenReturn(session);

        Mockito.when(cartService.addToCart(cart, 1)).thenReturn(item);

        Mockito.when(cartService.getSession(nonExistedToken))
                .thenThrow(new SessionNotFoundException("Session not found"));
    }

    @Test
    public void whenSessionIsPresent_thenReturnUserCart() throws Exception {

        mockMvc.perform(get("/api/v1/cart")
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
        Session emptyCartSession = new Session(userToken, LocalDateTime.now(), credentials);

        Mockito.when(cartService.getSession(userToken)).thenReturn(emptyCartSession);

        mockMvc.perform(get("/api/v1/cart")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    public void whenSessionIsNotAvailable_thenDoNotGetItemsListAndThrowSessionNotFoundException() throws Exception {

        mockMvc.perform(get("/api/v1/cart")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-doesNotExist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Session not found"));
    }

    @Test
    public void whenSessionAndItemAreAvailable_thenAddItemToCart() throws Exception {
        mockMvc.perform(post("/api/v1/cart/item/1")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andExpect(jsonPath("$.description").value(item.getDescription()));
    }

    @Test
    public void whenItemIsNotPresent_thenDoNotAddItemToCartAndThrowItemNotFoundException() throws Exception {
        Mockito.when(cartService.addToCart(cart, 2)).thenThrow(new ItemNotFountException("Item not available"));

        mockMvc.perform(post("/api/v1/cart/item/2")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Item not available"));
    }

    @Test
    public void whenSessionIsNotAvailable_thenDoNotAddItemToCartAndThrowSessionNotFoundException()
            throws Exception {

        mockMvc.perform(post("/api/v1/cart/item/1")
                        .param("userToken", "1c0451d1-12c7-43ff-9a3f-doesNotExist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Session not found"));
    }
}