package org.ogorodnik.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Credentials testCredentialsFromDb = Credentials.builder()
                .login("testLogin")
                .password("testPassword")
                .salt("testSalt")
                .build();
        userRepository.save(testCredentialsFromDb);
    }

    @Test
    @DisplayName("When Existed Login Provided, Return Optional Credentials")
    public void whenExistedLoginProvided_thenReturnOptionalCredentials() {
        Optional<Credentials> testCredentialsFromDb = userRepository.findByLoginIgnoreCase("testLogin");
        assertTrue(testCredentialsFromDb.isPresent());
        assertEquals("testLogin", testCredentialsFromDb.get().getLogin());
        assertEquals("testPassword", testCredentialsFromDb.get().getPassword());
        assertEquals("testSalt", testCredentialsFromDb.get().getSalt());
    }

    @Test
    @DisplayName("Return Optional Empty When non Existed Login Provided")
    public void whenNonExistedLoginProvided_thenReturnOptionalEmpty() {
        assertTrue(userRepository.findByLoginIgnoreCase("nonExistedLogin").isEmpty());
    }
}