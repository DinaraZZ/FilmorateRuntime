package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

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
        Optional<Film> filmOpt = findById(filmId);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();
            Set<Integer> likes = film.getLikes();
            // добавить проверку существования пользователя
            likes.add(userId);
//            film.setLikes(likes);
//            update(film);

            return film;
        } else throw new NotFoundException("Фильм не найден.");
    }

    public Film unlike(int filmId, int userId) {
        Optional<Film> filmOpt = findById(filmId);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();
            Set<Integer> likes = film.getLikes();
            // добавить проверку существования пользователя
            likes.remove(userId);
//            film.setLikes(likes);
//            update(film);

            return film;
        } else throw new NotFoundException("Фильм не найден.");
    }

    public List<Film> topLikedFilms(int count) {
        List<Film> films = findAll();
        films.sort((f1, f2) -> f2.getLikes().size() - f1.getLikes().size());
        return films.subList(0, Math.min(count, films.size()));
    }
}
