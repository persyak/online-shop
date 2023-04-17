package org.ogorodnik.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.repository.testcontainer.AbstractContainerBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRepositoryTest extends AbstractContainerBaseTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .name("testItemGlobal")
                .price(100.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescriptionGlobal")
                .build();

        itemRepository.save(item);
    }

    @Test
    public void testFindAll() {
        List<Item> found = (List<Item>) itemRepository.findAll();
        assertFalse(found.isEmpty());
        boolean containsTestItemGlobal = false;
        for (Item item : found) {
            if ("testItemGlobal".equalsIgnoreCase(item.getName())) {
                containsTestItemGlobal = true;
                break;
            }
        }
        assertTrue(containsTestItemGlobal);
    }

    @Test
    @DisplayName("Save Item to Database")
    public void whenSaveItem_thenReturnItem() {
        Item item = Item.builder()
                .name("testItemName")
                .price(20.0)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();

        Item itemFromDb = itemRepository.save(item);
        assertEquals("testItemName", itemFromDb.getName());
        assertEquals(20.0, itemFromDb.getPrice());
        assertEquals("testDescription", itemFromDb.getDescription());
    }

    @Test
    @DisplayName("Throw DbActionExecutionException when Item with Missing Attribute Provided")
    public void whenMissingAttributeItemProvided_thenThrowDbActionExecutionException() {
        Item item = Item.builder()
                .name("testItemName")
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .description("testDescription")
                .build();
        assertThrows(DbActionExecutionException.class, () -> {
            itemRepository.save(item);
        });
    }

    @Test
    @DisplayName("When Valid Id Provided, then Delete Item")
    public void whenValidIdProvided_thenDeleteItem() {
        List<Item> foundBefore = (List<Item>) itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "testItemGlobal", "testItemGlobal");
        itemRepository.deleteById(foundBefore.get(0).getId());
        List<Item> foundAfter = (List<Item>) itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "testItemGlobal", "testItemGlobal");
        assertTrue(foundAfter.isEmpty());
    }

    @Test
    @DisplayName("Search Item by Name when Valid Search Pattern Provided")
    public void whenExistedSearchPatternProvided_findItemByNameOrDescription() {
        List<Item> foundByName = (List<Item>) itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "itemglob", "itemglob");
        assertEquals("testItemGlobal", foundByName.get(0).getName());
        assertEquals("testDescriptionGlobal", foundByName.get(0).getDescription());

        List<Item> foundByDescription = (List<Item>) itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "description", "description");
        assertEquals("testItemGlobal", foundByName.get(0).getName());
        assertEquals("testDescriptionGlobal", foundByName.get(0).getDescription());
    }

    @Test
    @DisplayName("Return Empty Collection when non Existed Search Pattern Provided")
    public void whenNonExistedSearchPatternProvided_returnEmptyCollection() {
        List<Item> found = (List<Item>) itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "nonExisted", "nonExisted");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("When Existed Id Provided, Return Optional Item")
    public void whenExistedIdProvided_thenReturnOptionalItem() {
        List<Item> found = (List<Item>) itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "testItemGlobal", "testItemGlobal");

        Optional<Item> item = itemRepository.findById(found.get(0).getId());
        assertTrue(item.isPresent());
        assertEquals("testItemGlobal", item.get().getName());
    }

    @Test
    @DisplayName("When non Existed Id Provided, Return Optional Empty")
    public void whenNonExistedIdProvided_thenReturnEmptyOptional() {
        assertTrue(itemRepository.findById(-1L).isEmpty());
    }
}