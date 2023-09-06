package org.ogorodnik.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.BaseContainerImpl;
import org.ogorodnik.shop.security.entity.Credentials;
import org.ogorodnik.shop.security.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest extends BaseContainerImpl {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Credentials testCredentialsFromDb = Credentials.builder()
                .username("testUser")
                .password("testPassword")
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.USER)
                .build();
        userRepository.save(testCredentialsFromDb);
    }

    @Test
    @DisplayName("When Existed Login Provided, Return Optional Credentials")
    public void whenExistedLoginProvided_thenReturnOptionalCredentials() {
        Optional<Credentials> testCredentialsFromDb = userRepository.findByUsernameIgnoreCase("testUser");
        assertTrue(testCredentialsFromDb.isPresent());
        assertEquals("testUser", testCredentialsFromDb.get().getUsername());
        assertEquals("testPassword", testCredentialsFromDb.get().getPassword());
        assertEquals("firstname", testCredentialsFromDb.get().getFirstname());
        assertEquals("lastname", testCredentialsFromDb.get().getLastname());
        assertEquals(Role.USER, testCredentialsFromDb.get().getRole());
    }

    @Test
    @DisplayName("Return Optional Empty When non Existed Login Provided")
    public void whenNonExistedLoginProvided_thenReturnOptionalEmpty() {
        assertTrue(userRepository.findByUsernameIgnoreCase("nonExistedLogin").isEmpty());
    }
}