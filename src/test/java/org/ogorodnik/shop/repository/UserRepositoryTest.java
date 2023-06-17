package org.ogorodnik.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Credentials;
import org.ogorodnik.shop.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("test")
            .withPassword("password")
            .withDatabaseName("items");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

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