package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
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
    private final UserService userService;

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Optional<Film> findById(int id) {
        return filmStorage.findById(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film like(int filmId, int userId) {
        Film film = getById(filmId);
        userService.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().add(userId);

        return film;
    }

    public Film unlike(int filmId, int userId) {
        Film film = getById(filmId);
        userService.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().remove(userId);

        return film;
    }

    public List<Film> topLikedFilms(int count) {
        List<Film> films = findAll();
        films.sort((f1, f2) -> f2.getLikes().size() - f1.getLikes().size());
        return films.subList(0, Math.min(count, films.size()));
    }

    private Film getById(int id) {
        return filmStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Фильм не найден"));
    }
}
