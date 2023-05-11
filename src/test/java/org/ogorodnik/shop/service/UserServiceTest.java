package org.ogorodnik.shop.service;

import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Credentials testCredentialsFromDb = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .salt("testSalt")
                .build();

        Mockito.when(userRepository.findByLoginIgnoreCase("testLogin"))
                .thenReturn(Optional.ofNullable(testCredentialsFromDb));

        Mockito.when(userRepository.findByLoginIgnoreCase("absentLogin")).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("Get Credentials When ValidLogin Provided")
    public void whenExistedLoginProvided_thenReturnCredentials() throws AuthenticationException {
        Credentials credentials = userService.getCredentials("testLogin");
        assertEquals("testLogin", credentials.getLogin());
        assertEquals("testPassword", credentials.getPassword());
        assertEquals("testSalt", credentials.getSalt());
    }

    @Test
    public void whenAbsentLoginProvided_thenThrowAuthenticationException() {
        Exception exception = assertThrows(AuthenticationException.class, () -> {
            userService.getCredentials("absentLogin");
        });
        assertTrue(exception.getMessage().contains("User does not exist"));
    }
}