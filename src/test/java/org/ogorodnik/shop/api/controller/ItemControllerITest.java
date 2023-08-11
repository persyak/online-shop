package org.ogorodnik.shop.api.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.BaseContainerImpl;
import org.ogorodnik.shop.config.TestConfigurationToCountAllQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@AutoConfigureMockMvc
@Import(TestConfigurationToCountAllQueries.class)
public class ItemControllerITest extends BaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/item-dataset.json")
    public void testFindAll() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(get("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testItem"))
                .andExpect(jsonPath("$[0].price").value("10.0"))
                .andExpect(jsonPath("$[0].description").value("description"));

        assertSelectCount(1);
    }
}
