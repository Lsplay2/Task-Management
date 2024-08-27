package com.example.tm.controller;

import com.example.tm.entity.User;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.service.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;

@Tag(name = "Users", description = "For work with users")
@RestController

public class UserController {
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Gets all users", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the users",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    })
    })
    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на получение пользователей по id:" + id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Поступил запрос на создание пользователя:" + user);
        userService.saveUser(user);
        log.info("Пользователь добавлен в коллекцию:" + user);
        return user;
    }
}
