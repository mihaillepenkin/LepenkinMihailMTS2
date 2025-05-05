package org.example.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.TypesOfEvent;
import org.example.entity.Users;
import org.example.entity.Users;
import org.example.exception.BucketNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaService;

    public void registrationUser(String userName, String email, String password) throws JsonProcessingException {
        log.info("Функция создания юзера в сервисе");
        Users user = new Users(userName, email, password);
        userRepository.save(user);
        kafkaService.sendMessage(user.getUserId(), TypesOfEvent.CREATE);
    }

    public void deleteUser(Long id) throws UserNotFoundException, JsonProcessingException {
        log.info("Функция удаления юзера в сервисе");
        Users user = userRepository.findByUserIdEquals(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
        kafkaService.sendMessage(user.getUserId(), TypesOfEvent.DELETE);
    }
}
