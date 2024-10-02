package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 1;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");

        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        user.setId(counter++);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");

        if(!users.containsKey(user.getId())) throw new IllegalStateException("");
        users.put(user.getId(), user);
        return user;
    }
}
