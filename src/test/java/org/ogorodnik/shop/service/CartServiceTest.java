package org.ogorodnik.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private SecurityService securityService;
    LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    @BeforeEach
    void setUp() throws ItemNotFountException {
        Item item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(localDateTime)
                .description("testDescription")
                .build();

        Session session = new Session("existedToken", localDateTime);

        Mockito.when(itemService.getItemById(1L)).thenReturn(item);
        Mockito.when(itemService.getItemById(2L)).thenThrow(new ItemNotFountException("Item not available"));
        Mockito.when(securityService.getSession("existedToken")).thenReturn(Optional.of(session));
        Mockito.when(securityService.getSession("absentToken")).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("Return Item when Existed Id Provided")
    public void whenExistedItemIdProvided_thenAddItToCartAndReturn() throws ItemNotFountException {
        List<Item> cart = new ArrayList<>();
        Item item = cartService.addToCart(cart, 1L);
        assertEquals(1, cart.size());
        assertEquals("testItemName", item.getName());
        assertEquals(20.0, item.getPrice());
        assertEquals("testDescription", item.getDescription());
    }

    @Test
    @DisplayName("Throw ItemNotFoundException when Absent Id Provided")
    public void whenNotExistedItemIdProvided_thenThrowItemNotFoundException() {
        List<Item> cart = new ArrayList<>();

        Exception exception = assertThrows(ItemNotFountException.class, () -> {
            cartService.addToCart(cart, 2L);
        });
        assertTrue(exception.getMessage().contains("Item not available"));
    }

    @Test
    @DisplayName("Return Session when Valid Token is Provided")
    public void whenExistedTokenProvided_thenReturnSessionOptional() {
        Optional<Session> sessionOptional = cartService.getSession("existedToken");
        assertTrue(sessionOptional.isPresent());
        assertEquals("existedToken", sessionOptional.get().getUserToken());
        assertEquals(localDateTime, sessionOptional.get().getExpireDate());
        assertInstanceOf(CopyOnWriteArrayList.class, sessionOptional.get().getCart());
    }

    @Test
    @DisplayName("Get Empty Optional when Absent Token is Provided")
    public void whenAbsentTokenProvided_thenReturnEmptyOptional() {
        assertTrue(cartService.getSession("absentToken").isEmpty());
    }
}