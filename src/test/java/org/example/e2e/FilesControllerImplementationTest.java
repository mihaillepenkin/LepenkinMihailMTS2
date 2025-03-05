package org.example.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SpringProjectApplicationTest {

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;

    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withInitScript("bdTestHomework.sql")
                    .withDatabaseName("dbTest")
                    .withUsername("admin")
                    .withPassword("secret");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void EndToEndTest() {
        log.info("Db host: {}", postgresContainer.getHost());
        log.info("Db port: {}", postgresContainer.getFirstMappedPort());
        log.info("PostgreSQL is running at: {}", postgresContainer.getJdbcUrl());

        String renameFileRequest =
                restTemplate.patchForObject("http://localhost:" + port + "/files/info/Russia/Turkey","",String.class);

        assertEquals("переименовываю файл Russia в Turkey", renameFileRequest);

        ResponseEntity<String> deleteFileRequest =
                restTemplate.exchange("http://localhost:" + port + "/files/info/USA", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);


        String replaceFileRequest =
                restTemplate.patchForObject("http://localhost:" + port + "/files/info/Country/Turkey/Book","",String.class);

        assertEquals("перемещаю файл Turkey из Country в Book", replaceFileRequest);

        ResponseEntity<String> deleteBucketRequest =
                restTemplate.exchange("http://localhost:" + port + "/buckets/info/Country", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        String renameBucketRequest =
                restTemplate.patchForObject("http://localhost:" + port + "/buckets/info/Book/Paper","",String.class);

        assertEquals("переименовываю бакет Book в Paper", renameBucketRequest);


    }
}