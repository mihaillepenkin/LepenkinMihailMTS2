package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "UserUtils API", description = "User utils")

public interface UserController {
    @Operation(summary = "registration user")
    @ApiResponse(responseCode = "200", description = "user is registered")
    @Parameter(in = ParameterIn.PATH, name = "userName", description = "user name", example = "Misha")
    @Parameter(in = ParameterIn.PATH, name = "email", description = "email", example = "mail@mail.mail")
    @Parameter(in = ParameterIn.PATH, name = "password", description = "password", example = "secret_code")
    public ResponseEntity<String> registrationUser(@PathVariable("userName") String userName, @PathVariable("email") String email, @PathVariable("password") String password) throws JsonProcessingException;

    @Operation(summary = "delete user")
    @ApiResponse(responseCode = "200", description = "user is deleted")
    @Parameter(in = ParameterIn.PATH, name = "userId", description = "userId", example = "1155998")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") long userId) throws UserNotFoundException, JsonProcessingException;

}