package org.ogorodnik.shop.api.controller;

import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.security.SecurityService;
import org.ogorodnik.shop.security.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginLogoutController.class)
class LoginLogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;

    private Credentials credentials;

    @BeforeEach
    void setUp() {
        credentials = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .build();
    }

    @Test
    public void whenValidCredentials_thenPerformLogin() throws Exception {
        LocalDateTime expireDate = LocalDateTime.now();
        Session session = new Session("1c0451d1-12c7-43ff-9a3f-d5ae73e18e36", expireDate);

        Mockito.when(securityService.login(credentials)).thenReturn(session);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testLogin",
                                    "password": "testPassword"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("userToken"))
                .andExpect(jsonPath("$.value").value(session.getUserToken()))
                .andExpect(jsonPath("$.maxAge").value(14400));
    }

    @Test
    public void whenInvalidCredentials_thenThrowAuthenticationException() throws Exception {

        Credentials nonExistedCredentials = Credentials.builder()
                .login("nonExistedLogin")
                .password("nonExistedPassword")
                .build();

        Mockito.when(securityService.login(nonExistedCredentials))
                .thenThrow(new AuthenticationException("User does not exist"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "nonExistedLogin",
                                    "password": "nonExistedPassword"
                                }"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("User does not exist"));
    }

    @Test
    public void whenInvalidPassword_thenThrowAuthenticationException() throws Exception {

        Credentials BadPasswordCredentials = Credentials.builder()
                .login("testLogin")
                .password("nonExistedPassword")
                .build();

        Mockito.when(securityService.login(BadPasswordCredentials))
                .thenThrow(new AuthenticationException("Password is not correct"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testLogin",
                                    "password": "nonExistedPassword"
                                }"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Password is not correct"));
    }

    @Test
    public void whenLoggedIn_thenLogout() throws Exception {

        Mockito.when(securityService.logout("1c0451d1-12c7-43ff-9a3f-d5ae73e18e36")).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/api/v1/logout")
                .param("cookie", "1c0451d1-12c7-43ff-9a3f-d5ae73e18e36"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("logout", result.getResponse().getContentAsString());
    }

    @Test
    public void whenNotLoggedIn_thenReturnNotLoggedInMessage() throws Exception {

        Mockito.when(securityService.logout("xxx-xxx-xxx")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/api/v1/logout")
                        .param("cookie", "xxx-xxx-xxx"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("notLoggedIn", result.getResponse().getContentAsString());
    }
}