package org.ogorodnik.shop.security;

import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityServiceTest {
    @Autowired
    private SecurityService securityService;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() throws AuthenticationException {

        String testSalt = BCrypt.gensalt();

        Credentials testCredentialsFromDb = Credentials.builder()
                .login("testUser")
                .password(BCrypt.hashpw("testPassword", testSalt))
                .salt(testSalt)
                .build();

        Mockito.when(userService.getUserPassword("testUser"))
                .thenReturn(testCredentialsFromDb);

        Mockito.when(userService.getUserPassword("notExistedUser"))
                .thenThrow(new AuthenticationException("User does not exist"));
    }

    @Test
    @DisplayName("Get Session when Valid Credentials are Provided")
    public void whenValidCredentialsProvided_thenReturnSessionOptional() throws AuthenticationException {

        Credentials testCredentials = Credentials.builder()
                .login("testUser")
                .password("testPassword")
                .build();

        Session loginSession = securityService.login(testCredentials);

        assertTrue(Objects.nonNull(loginSession));
        assertTrue(loginSession.getExpireDate().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Throw AuthenticationException when user was not found")
    public void whenNotExistedUserProvided_thenThrowAuthenticationException() throws AuthenticationException {
        Credentials testCredentials = Credentials.builder()
                .login("notExistedUser")
                .build();

        Exception exception = assertThrows(AuthenticationException.class, () -> {
            securityService.login(testCredentials);
        });
        assertTrue(exception.getMessage().contains("User does not exist"));
    }

    @Test
    public void whenPasswordIsIncorrect_thenThrowAuthenticationException() {
        Credentials inValidPasswordCredentials = Credentials.builder()
                .login("testUser")
                .password("invalidPassword")
                .build();

        Exception exception = assertThrows(AuthenticationException.class, () -> {
            securityService.login(inValidPasswordCredentials);
        });
        assertTrue(exception.getMessage().contains("Password is not correct"));
    }

    @Test
    public void getSession() {
        String testUserToken = "XXXYYY";
        assert (securityService.getSession(testUserToken).isEmpty());
    }
}