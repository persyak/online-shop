package org.ogorodnik.shop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.error.ItemNotFountException;
import org.ogorodnik.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {

        LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Item itemFirst = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(creationDate)
                .description("testDescription")
                .build();

        Item itemSecond = Item.builder()
                .id(2L)
                .name("testItemNameSecond")
                .price(30.0)
                .creationDate(creationDate)
                .description("testDescriptionSecond")
                .build();

        Item itemUpdated = Item.builder()
                .id(1L)
                .name("testItemNameSecond")
                .price(30.0)
                .creationDate(creationDate)
                .description("testDescriptionSecond")
                .build();

        Item itemUpdatedWithLegacyName = Item.builder()
                .id(1L)
                .name("testItemName")
                .price(30.0)
                .creationDate(creationDate)
                .description("testDescriptionSecond")
                .build();

        Item itemUpdatedWithLegacyPrice = Item.builder()
                .id(1L)
                .name("testItemNameSecond")
                .price(20.0)
                .creationDate(creationDate)
                .description("testDescriptionSecond")
                .build();

        Item itemUpdatedWithLegacyDescription = Item.builder()
                .id(1L)
                .name("testItemNameSecond")
                .price(30.0)
                .creationDate(creationDate)
                .description("testDescription")
                .build();

        Mockito.when(itemRepository.findAll()).thenReturn(List.of(itemFirst, itemSecond));

        Mockito.when(itemRepository.save(Item.builder()
                        .id(1L)
                        .name("testItemName")
                        .price(20.0)
                        .creationDate(creationDate)
                        .description("testDescription")
                        .build()))
                .thenReturn(itemFirst);

        Mockito.when(itemRepository.save(Item.builder()
                        .id(1L)
                        .name("testItemNameSecond")
                        .price(30.0)
                        .creationDate(creationDate)
                        .description("testDescriptionSecond")
                        .build()))
                .thenReturn(itemUpdated);

        Mockito.when(itemRepository.save(Item.builder()
                        .id(1L)
                        .name("testItemName")
                        .price(30.0)
                        .creationDate(creationDate)
                        .description("testDescriptionSecond")
                        .build()))
                .thenReturn(itemUpdatedWithLegacyName);

        Mockito.when(itemRepository.save(Item.builder()
                        .id(1L)
                        .name("testItemNameSecond")
                        .price(20.0)
                        .creationDate(creationDate)
                        .description("testDescriptionSecond")
                        .build()))
                .thenReturn(itemUpdatedWithLegacyPrice);

        Mockito.when(itemRepository.save(Item.builder()
                        .id(1L)
                        .name("testItemNameSecond")
                        .price(30.0)
                        .creationDate(creationDate)
                        .description("testDescription")
                        .build()))
                .thenReturn(itemUpdatedWithLegacyDescription);

        Mockito.when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "testItem", "testItem"))
                .thenReturn(List.of(itemFirst));

        Mockito.when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "description", "description"))
                .thenReturn(List.of(itemFirst));

        Mockito.when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "nonExistedItem", "nonExistedItem"))
                .thenReturn(List.of());

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(itemFirst));
    }

    @Test
    @DisplayName("Get List of Two Items on findAll call")
    public void whenFindAll_thenReturnListOfTwoItems() {
        List<Item> found = (List<Item>) itemService.findAll();
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Get itemFirst when addItem call")
    public void whenAddItem_thenReturnItemFirst() {
        Item found = itemService.addItem(Item.builder()
                .id(1L)
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build());

        assertEquals("testItemName", found.getName());
    }

    @Test
    @DisplayName("Test itemFirst is update with itemSecond")
    public void whenItemFirstUpdateByItemSecond_thenItemUpdatedReturned() throws ItemNotFountException {
        Item updateFrom = Item.builder()
                .name("testItemNameSecond")
                .price(30.0)
                .description("testDescriptionSecond")
                .build();
        Item updated = itemService.updateItem(1L, updateFrom);
        assertEquals(1L, updated.getId());
        assertEquals("testItemNameSecond", updated.getName());
        assertEquals(30.0, updated.getPrice());
        assertEquals("testDescriptionSecond", updated.getDescription());
    }

    @Test
    @DisplayName("If name is not updated in item, return updated item without name updated")
    public void whenUpdateItemFirstWithItemSecondWithoutName_thenItemWithoutUpdatedNameReturned()
            throws ItemNotFountException {
        Item updateFrom = Item.builder()
                .price(30.0)
                .description("testDescriptionSecond")
                .build();
        Item updated = itemService.updateItem(1L, updateFrom);
        assertEquals(1L, updated.getId());
        assertEquals("testItemName", updated.getName());
        assertEquals(30.0, updated.getPrice());
        assertEquals("testDescriptionSecond", updated.getDescription());
    }

    @Test
    @DisplayName("If price is not updated in item, return updated item without price updated")
    public void whenUpdateItemFirstWithItemSecondWithoutPrice_thenItemWithoutUpdatedPriceReturned()
            throws ItemNotFountException {
        Item updateFrom = Item.builder()
                .name("testItemNameSecond")
                .description("testDescriptionSecond")
                .build();
        Item updated = itemService.updateItem(1L, updateFrom);
        assertEquals(1L, updated.getId());
        assertEquals("testItemNameSecond", updated.getName());
        assertEquals(20.0, updated.getPrice());
        assertEquals("testDescriptionSecond", updated.getDescription());
    }

    @Test
    @DisplayName("If description is not updated in item, return updated item without description updated")
    public void whenUpdateItemFirstWithItemSecondWithoutDescription_thenItemWithoutUpdatedDescriptionReturned()
            throws ItemNotFountException {
        Item updateFrom = Item.builder()
                .name("testItemNameSecond")
                .price(30.0)
                .build();
        Item updated = itemService.updateItem(1L, updateFrom);
        assertEquals(1L, updated.getId());
        assertEquals("testItemNameSecond", updated.getName());
        assertEquals(30.0, updated.getPrice());
        assertEquals("testDescription", updated.getDescription());
    }

    @Test
    @DisplayName("ItemNotFoundException is thrown when item to update is not available")
    public void whenAbsentItemIsUpdated_thenItemNotFoundExceptionIsThrown() {
        Exception exception = assertThrows(ItemNotFountException.class, () -> {
            itemService.updateItem(3L, new Item());
        });
        assertTrue(exception.getMessage().contains("Item not available"));
    }

    @Test
    @DisplayName("Get Data based on found Name by Pattern")
    public void whenValidSearchPattern_thenItemShouldFoundByName() {
        Iterable<Item> found = itemService.findByNameOrDescription("testItem");
        assertEquals("testItemName", found.iterator().next().getName());
    }

    @Test
    @DisplayName("Get Data based on found Description by Pattern")
    public void whenValidSearchPattern_thenItemShouldFoundByDescription() {
        Iterable<Item> found = itemService.findByNameOrDescription("description");
        assertEquals("testDescription", found.iterator().next().getDescription());
    }

    @Test
    @DisplayName("No Data found by Invalid Pattern")
    public void whenInvalidSearchPattern_thenNoItemShouldFound() {
        Iterable<Item> found = itemService.findByNameOrDescription("nonExistedItem");
        assertFalse(found.iterator().hasNext());
    }

    @Test
    @DisplayName("Get item by id")
    public void testGetItemById() throws ItemNotFountException {
        assertEquals("testItemName", itemService.getItemById(1L).getName());
    }

    @Test
    @DisplayName("ItemNotFoundException is thrown when item is not available by id")
    public void whenAbsentId_thenItemNotFoundExceptionThrown() {
        Exception exception = assertThrows(ItemNotFountException.class, () -> {
            itemService.getItemById(3L);
        });
        assertTrue(exception.getMessage().contains("Item not available"));
    }

    @Test
    public void whenNonExistedItemIdProvided_thenThrowItemNotFountExceptionWhenDeletingItem() {
        Exception exception = assertThrows(ItemNotFountException.class, () -> {
            itemService.deleteItemById(3L);
        });
        assertTrue(exception.getMessage().contains("Item not available"));
    }
}