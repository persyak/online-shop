package org.ogorodnik.shop.api.controller;

import com.github.database.rider.core.api.dataset.DataSet;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfigurationToCountAllQueries.class)
public class ProcessUserCartControllerITest extends BaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;

    private static final LocalDateTime DATE_TIME =
            LocalDateTime.of(2023, 8, 11, 11, 35, 35);

    @Test
    @WithMockUser(authorities = "USER")
    @DataSet(value = "datasets/usercart/add-item-to-cart.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    public void testAddItemToCart() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(post("/api/v1/cart/item/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("testItem"))
                .andExpect(jsonPath("$.price").value("10.0"))
                .andExpect(jsonPath("$.creationDate").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$.description").value("description"));

        assertSelectCount(1);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void whenSessionIsPresent_thenReturnUserCart() throws Exception {

        mockMvc.perform(get("/api/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}
