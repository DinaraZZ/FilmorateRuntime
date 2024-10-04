package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 1;

    @Override
    public User add(User entity) {
        if (entity.getName() == null || entity.getName().isBlank()) entity.setName(entity.getLogin());
        entity.setId(counter++);
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User update(User entity) {
        if(!users.containsKey(entity.getId())) throw new IllegalStateException("");
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }
}
