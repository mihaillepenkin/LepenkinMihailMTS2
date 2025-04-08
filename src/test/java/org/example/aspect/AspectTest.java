package org.example.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AspectTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LoggingAspect loggingAspect;

    @Test
    void shouldIncreaseClassFieldByTwo() {
        int fieldValueBeforeExecution = loggingAspect.getExecutionCount();
        String response = restTemplate.patchForObject("http://localhost:" + port + "/files/info/Russia/Turkey", "",String.class);
        log.info(response);
        assertEquals(fieldValueBeforeExecution + 2, loggingAspect.getExecutionCount());
    }
}