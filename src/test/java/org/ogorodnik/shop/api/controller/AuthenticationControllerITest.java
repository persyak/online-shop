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
import org.springframework.test.web.servlet.MockMvc;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Import(TestConfigurationToCountAllQueries.class)
public class AuthenticationControllerITest extends BaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @ExpectedDataSet(value = "datasets/authentication/register-user-dataset.json")
    public void registerUserTest() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "userName",
                                    "password": "password",
                                    "firstname": "firstName",
                                    "lastname": "lastName",
                                    "role": "ADMIN"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        assertInsertCount(1);
    }

    @Test
    @DataSet(value = "datasets/authentication/authentication-user-dataset.json",
            cleanAfter = true, cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenValidCredentialsProvided_thenAuthenticateAndReturnToken() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "userName",
                                    "password": "password"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        assertSelectCount(2);
    }

    @Test
    @DataSet(value = "datasets/authentication/authentication-user-dataset.json",
            cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    public void whenInvalidUserName_thenThrowAuthenticationException() throws Exception {

        SQLStatementCountValidator.reset();

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "invalidUser",
                                    "password": "invalidPassword"
                                }"""))
                .andExpect(status().isForbidden());

        assertSelectCount(1);
    }
}
