package org.ogorodnik.shop;

import com.github.database.rider.spring.api.DBRider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
public abstract class BaseContainerImpl {

    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("test")
                .withPassword("password")
                .withDatabaseName("items");

        container.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
}
