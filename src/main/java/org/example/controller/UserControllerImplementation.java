package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.exception.FileNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserControllerImplementation implements UserController{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    private final UserService userService;


    public UserControllerImplementation(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping("/users/create/{userName}/{email}/{password}")
    public ResponseEntity<String> registrationUser(String userName, String email, String password) throws JsonProcessingException {
        userService.registrationUser(userName, email, password);
        return ResponseEntity.ok("зарегестрировал юзера " + userName);
    }

    @Override
    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<String> deleteUser(long userId) throws UserNotFoundException, JsonProcessingException {
        userService.deleteUser(userId);
        return ResponseEntity.ok("удалил юзера " + userId);
    }
}
