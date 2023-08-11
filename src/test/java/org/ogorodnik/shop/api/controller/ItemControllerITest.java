package org.ogorodnik.shop.api.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.BaseContainerImpl;
import org.ogorodnik.shop.config.TestConfigurationToCountAllQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfigurationToCountAllQueries.class)
public class ItemControllerITest extends BaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;

    private static final LocalDateTime DATE_TIME =
            LocalDateTime.of(2023, 8, 11, 11, 35, 35);

    @Test
    @DataSet(value = "datasets/item-dataset.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    public void testFindAll() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(get("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testItem"))
                .andExpect(jsonPath("$[0].price").value("10.0"))
                .andExpect(jsonPath("$[0].creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$[0].description").value("description"));

        assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/item-search.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    @WithMockUser(authorities = "USER")
    public void whenItemIsPresent_thenFIndItemByNameAndDescription() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(get("/api/v1/items/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[0].name").value("searchItem"))
                .andExpect(jsonPath("$[0].price").value("20.0"))
                .andExpect(jsonPath("$[0].creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$[0].description").value("searchItemDescription"));

        assertSelectCount(1);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenItemDoesNotExist_thenReturnEmptyList() throws Exception {
        SQLStatementCountValidator.reset();

        mockMvc.perform(get("/api/v1/items/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        assertSelectCount(1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @ExpectedDataSet(value = "datasets/item-dataset.json")
    public void whenAddItem_thenAddedItemReturnedAndOkStatusReceived() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(post("/api/v1/items/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testItem",
                                    "price": 10.0,
                                    "creationDate": "2023-08-11T11:35:35.000",
                                    "description": "description"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("testItem"))
                .andExpect(jsonPath("$.price").value("10.0"))
                .andExpect(jsonPath("$.creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$.description").value("description"));

        assertInsertCount(1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DataSet(value = "datasets/item-dataset.json",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "datasets/item-updated.json")
    public void whenValidIdProvided_thenUpdateAndReturnUpdatedItem() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(put("/api/v1/items/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "testItemName",
                                    "price": 20.0,
                                    "description": "testDescription"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("testItemName"))
                .andExpect(jsonPath("$.price").value("20.0"))
                .andExpect(jsonPath("$.creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$.description").value("testDescription"));

        assertSelectCount(1);
        assertUpdateCount(1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DataSet(value = "datasets/item-dataset.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "datasets/item-dataset.json")
    public void whenNonExistedIdProvidedDuringItemUpdate_thenThrowItemNotFountException() throws Exception {

        SQLStatementCountValidator.reset();

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

        assertSelectCount(1);
        assertUpdateCount(0);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DataSet(value = "datasets/item-dataset.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "datasets/item-deleted.json")
    public void testDeleteItemById() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(delete("/api/v1/items/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("testItem"))
                .andExpect(jsonPath("$.price").value("10.0"))
                .andExpect(jsonPath("$.creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$.description").value("description"));

        assertDeleteCount(1);
    }
}
