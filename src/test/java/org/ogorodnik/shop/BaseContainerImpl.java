package org.ogorodnik.shop;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseContainerImpl {

    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("test")
                .withPassword("password")
                .withDatabaseName("items");

        container.start();
    }

//    @Autowired
//    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

//    protected String getResponseAsString(String jsonPath) {
//        URL resource = getClass().getClassLoader().getResource(jsonPath);
//        try {
//            return FileUtils.readFileToString(new File(resource.toURI()), StandardCharsets.UTF_8);
//        } catch (IOException | URISyntaxException e) {
//            throw new RuntimeException("Unable to find file: " + jsonPath);
//        }
//    }
//
//    protected <T> List<T> getMockedListObjects(String mockPath, Class<T> contentClass) {
//        URL resource = getClass().getClassLoader().getResource(mockPath);
//        try {
//            String fileContent = FileUtils.readFileToString(new File(resource.toURI()), StandardCharsets.UTF_8);
//            return objectMapper.readValue(fileContent,
//                    TypeFactory
//                            .defaultInstance()
//                            .constructParametricType(List.class, contentClass));
//        } catch (IOException | URISyntaxException e) {
//            throw new RuntimeException("Unable to find file: " + mockPath);
//        }
//    }
}
