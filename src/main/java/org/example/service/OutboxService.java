package org.example.service;


import org.example.TypesOfEvent;
import org.example.entity.Outbox;
import org.example.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final KafkaProducerService producerService;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() throws JsonProcessingException {
        List<Outbox> result = outboxRepository.findAll();
        for (Outbox outbox : result) {
            String[] resultSplit = outbox.getValue().split(" ");
            Long id = Long.parseLong(resultSplit[0]);
            TypesOfEvent action = TypesOfEvent.valueOf(resultSplit[1]);
            producerService.sendMessage(id, action);
        }
        outboxRepository.deleteAll(result);
    }
}
