package org.ogorodnik.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.security.entity.Credentials;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.security.entity.Role;
import org.ogorodnik.shop.exception.ItemNotFountException;
import org.ogorodnik.shop.exception.TokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DefaultCartServiceTest {

    @Autowired
    private CartService cartService;
    @MockBean
    private ItemService itemService;
    LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(localDateTime)
                .description("testDescription")
                .build();

        Credentials credentials = Credentials.builder()
                .username("testLogin")
                .password("testPassword")
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.USER)
                .build();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials, null, credentials.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        Mockito.when(itemService.getItemById(1L)).thenReturn(item);
        Mockito.when(itemService.getItemById(2L)).thenThrow(new ItemNotFountException("Item not available"));
    }

    @Test
    @DisplayName("Return Item when Existed Id Provided")
    public void whenExistedItemIdProvided_thenAddItToCartAndReturn() {
        Item item = cartService.addToCart(1L);
        assertEquals("testItemName", item.getName());
        assertEquals(20.0, item.getPrice());
        assertEquals("testDescription", item.getDescription());
    }

    @Test
    @DisplayName("Throw ItemNotFoundException when Absent Id Provided")
    public void whenNotExistedItemIdProvided_thenThrowItemNotFoundException() {
        Exception exception = assertThrows(ItemNotFountException.class, () -> {
            cartService.addToCart(2L);
        });
        assertTrue(exception.getMessage().contains("Item not available"));
    }

    @Test
    @DisplayName("Throw TokenNotFoundException when token is not available")
    public void whenTokenIsNotAvailable_thenThrowTokenNotFoundException() {
        SecurityContextHolder.clearContext();

        Exception exception = assertThrows(TokenNotFoundException.class, () -> {
            cartService.addToCart(1L);
        });
        assertTrue(exception.getMessage().contains("Token was not found. Ensure user is authorised"));
    }
}