package org.example.e2e;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.junit.jupiter.api.Assertions;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringProjectApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void endToEnd() {
        ResponseEntity<String> response1 =
                restTemplate.getForEntity("http://localhost:" + port + "/files/info/oldFileName/newFileName", String.class);
        Assertions.assertEquals(HttpStatus.OK, response1.getStatusCode());
        Assertions.assertTrue(response1.getBody().contains("переименовываю файл oldFileName в newFileName"));
        ResponseEntity<String> response2 =
                restTemplate.getForEntity("http://localhost:" + port + "/files/info/oldBucketName/fileName/newBucketName", String.class);
        Assertions.assertEquals(HttpStatus.OK, response2.getStatusCode());
        Assertions.assertTrue(response2.getBody().contains("перемещаю файл fileName из oldBucketName в newBucketName"));
        ResponseEntity<String> response3 =
                restTemplate.getForEntity("http://localhost:" + port + "/files/info/fileName", String.class);
        Assertions.assertEquals(HttpStatus.OK, response3.getStatusCode());
        Assertions.assertTrue(response3.getBody().contains("удаляю файл fileName"));
        ResponseEntity<String> response4 =
                restTemplate.getForEntity("http://localhost:" + port + "/buckets/info/bucketName", String.class);
        Assertions.assertEquals(HttpStatus.OK, response4.getStatusCode());
        Assertions.assertTrue(response4.getBody().contains("удаляю бакет bucketName"));
        ResponseEntity<String> response5 =
                restTemplate.getForEntity("http://localhost:" + port + "/buckets/info/oldBucketName/newBucketName", String.class);
        Assertions.assertEquals(HttpStatus.OK, response5.getStatusCode());
        Assertions.assertTrue(response5.getBody().contains("переименовываю бакет oldBucketName в newBucketName"));
    }
}