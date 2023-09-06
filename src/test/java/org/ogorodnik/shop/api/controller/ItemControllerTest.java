package org.ogorodnik.shop.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.exception.ItemNotFountException;
import org.ogorodnik.shop.service.implementation.DefaultItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultItemService itemService;

    private Item item;
    private List<Item> list;

    @BeforeEach
    void setUp() {

        item = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        list = List.of(item);
    }

    @Test
    public void testFindAll() throws Exception {
        Mockito.when(itemService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testItemName"))
                .andExpect(jsonPath("$[0].price").value("20.0"))
                .andExpect(jsonPath("$[0].description").value("testDescription"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenExistedSearchCriteria_thenFindByNameOrDescription() throws Exception {
        Mockito.when(itemService.findByNameOrDescription("test")).thenReturn(list);

        mockMvc.perform(get("/api/v1/items/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testItemName"))
                .andExpect(jsonPath("$[0].price").value("20.0"))
                .andExpect(jsonPath("$[0].description").value("testDescription"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenNonExistedSearchCriteria_thenReturnEmptyList() throws Exception {
        list = List.of();
        Mockito.when(itemService.findByNameOrDescription("nonExisted")).thenReturn(list);
        mockMvc.perform(get("/api/v1/items/nonExisted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAddItem() throws Exception {
        Item inputItem = Item.builder()
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        Mockito.when(itemService.addItem(inputItem)).thenReturn(item);

        mockMvc.perform(post("/api/v1/items/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testItemName",
                                    "price": 20.0,
                                    "description": "testDescription"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andExpect(jsonPath("$.description").value(item.getDescription()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenValidIdProvided_thenUpdateAndReturnUpdatedItem() throws Exception {
        Item itemToUpdate = Item.builder()
                .name("testItemName")
                .price(20.0)
                .description("testDescription")
                .build();

        Mockito.when(itemService.updateItem(1L, itemToUpdate)).thenReturn(item);

        mockMvc.perform(put("/api/v1/items/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testItemName",
                                    "price": 20.0,
                                    "description": "testDescription"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andExpect(jsonPath("$.description").value(item.getDescription()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenNonExistedIdProvidedDuringItemUpdate_thenThrowItemNotFountException() throws Exception {
        Item itemToUpdate = Item.builder()
                .name("testItemName")
                .price(20.0)
                .description("testDescription")
                .build();

        Mockito.when(itemService.updateItem(2L, itemToUpdate))
                .thenThrow(new ItemNotFountException("Item not available"));

        mockMvc.perform(put("/api/v1/items/edit/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testItemName",
                                    "price": 20.0,
                                    "description": "testDescription"
                                }"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Item not available"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testDeleteItemById() throws Exception {
        Mockito.when(itemService.deleteItemById(1L)).thenReturn(item);

        mockMvc.perform(delete("/api/v1/items/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andExpect(jsonPath("$.description").value(item.getDescription()));
    }
}