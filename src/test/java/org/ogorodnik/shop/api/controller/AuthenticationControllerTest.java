package org.ogorodnik.shop.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.api.dto.AuthenticationRequest;
import org.ogorodnik.shop.api.dto.AuthenticationResponse;
import org.ogorodnik.shop.api.dto.RegisterRequest;
import org.ogorodnik.shop.security.entity.Role;
import org.ogorodnik.shop.service.implementation.DefaultAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DefaultAuthenticationService defaultAuthenticationService;
    private AuthenticationResponse response;

    @BeforeEach
    void setUp() {
        response = AuthenticationResponse.builder()
                .token("testToken")
                .build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("firstName")
                .lastname("lastName")
                .username("userName")
                .password("password")
                .role(Role.ADMIN)
                .build();

        Mockito.when(defaultAuthenticationService.register(registerRequest)).thenReturn(response);

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
                .andExpect(jsonPath("$.token").value("testToken"));
    }

    @Test
    public void whenValidCredentialsProvided_thenAuthenticateAndReturnToken() throws Exception {

        AuthenticationRequest validRequest = AuthenticationRequest.builder()
                .username("validUser")
                .password("validPassword")
                .build();

        Mockito.when(defaultAuthenticationService.authenticate(validRequest)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "validUser",
                                    "password": "validPassword"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));
    }

    @Test
    public void whenInvalidUserName_thenThrowAuthenticationException() throws Exception {

        AuthenticationRequest invalidRequest = AuthenticationRequest.builder()
                .username("invalidUser")
                .password("invalidPassword")
                .build();

        Mockito.when(defaultAuthenticationService.authenticate(invalidRequest))
                .thenThrow(new BadCredentialsException("User not found"));

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "invalidUser",
                                    "password": "invalidPassword"
                                }"""))
                .andExpect(status().isForbidden());
    }
}