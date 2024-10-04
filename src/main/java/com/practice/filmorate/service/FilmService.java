package com.practice.filmorate.service;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    Film add(Film film) {
        return filmStorage.add(film);
    }

    Film update(Film film) {
        return filmStorage.update(film);
    }

    Optional<Film> findById(int id) {
        return filmStorage.findById(id);
    }

    List<Film> findAll() {
        return filmStorage.findAll();
    }

    Film like
}
