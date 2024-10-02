package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Film;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FilmStorageImpl implements com.practice.filmorate.storage.FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int counter = 1;

    @Override
    public Film add(Film entity) {
        return null;
    }

    @Override
    public Film update(Film entity) {
        return null;
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Film> findAll() {
        return null;
    }
}
