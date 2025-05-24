package org.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.TypesOfEvent;
import org.example.entity.Outbox;
import org.example.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest(properties = {"topic-to-send-message=test-topic"})
@Import({OutboxService.class, KafkaProducerService.class, KafkaAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class OutboxServiceTest {
    @TestConfiguration
    static class ObjectMapperTestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Container
    @ServiceConnection
    public static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private OutboxService outboxService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OutboxRepository outboxRepository;

    private static KafkaTestConsumer consumer;

    @BeforeAll
    static void setUp() {
        consumer = new KafkaTestConsumer(KAFKA.getBootstrapServers(), "test-group");
        consumer.subscribe(List.of("test-topic"));
    }

    @Test
    void shouldSendMessageToKafkaSuccessfully() {
        outboxRepository.save(new Outbox(1L, TypesOfEvent.INSERT));

        assertDoesNotThrow(() -> outboxService.processOutbox());

        KafkaTestConsumer consumer =
                new KafkaTestConsumer(KAFKA.getBootstrapServers(), "some-group-id");
        consumer.subscribe(List.of("test-topic"));

        ConsumerRecords<String, String> records = consumer.poll();
        assertEquals(1, records.count());
        records
                .iterator()
                .forEachRemaining(
                        record -> {
                            String message = null;
                            try {
                                JsonNode rootNode = objectMapper.readTree(record.value());
                                message = objectMapper.treeToValue(rootNode.get("value"), String.class);
                                System.out.println(message);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            assertEquals(TypesOfEvent.INSERT, TypesOfEvent.valueOf(message.split(" ")[1]));
                            assertEquals(1L, Long.parseLong(message.split(" ")[0]));
                        });
    }

    @Test
    void shouldFailToSendMessage() {
        assertThrows(
                IllegalArgumentException.class, () -> outboxRepository.save(new Outbox(null, null)));
        ConsumerRecords<String, String> records = consumer.poll();
        assertEquals(0, records.count());
    }
}