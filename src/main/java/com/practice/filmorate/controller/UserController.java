package com.practice.filmorate.controller;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users: {}", user);

        return userService.add(user);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");

        return userService.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос PUT /users/{}/friends/{}", id, friendId);

        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE /users/{}/friends/{}", id, friendId);

        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> findFriends(@PathVariable int id) {
        log.debug("Получен запрос GET /users/{}/friends", id);

        return userService.findFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET /users/{}/friends/common/{}", id, otherId);

        return userService.findCommonFriends(id, otherId);
    }

   /* @GetMapping("/users/{id}")
    public User findById(@PathVariable int id) {
        return User.builder()
                .id(1)
                .email("dafaf@fg.ru")
                .login("sag")
                .birthday(LocalDate.of(2000,1,1))
                .name("Name").build();
    }*/
}
