package com.practice.filmorate.controller;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
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

    @GetMapping("/{id}") // ?
    public User findById(@PathVariable int id) {
        log.debug("Получен запрос GET /users/{}", id);
        return userService.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) {
        log.debug("Получен запрос PUT /users/{}/friends/{}", id, friendId);
        if (id != friendId) {
            userService.addFriend(id, friendId);
        } else {
            throw new ValidationException("Id пользователя и друга не могут совпадать");
        }
    }

    @PutMapping("/{id}/friends")
    public void addFriendWithoutId(@PathVariable Integer id) {
        log.debug("Получен запрос PUT /users/{}/friends", id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE /users/{}/friends/{}", id, friendId);

        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable int id) {
        log.debug("Получен запрос GET /users/{}/friends", id);

        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET /users/{}/friends/common/{}", id, otherId);

        return userService.findCommonFriends(id, otherId);
    }
}
