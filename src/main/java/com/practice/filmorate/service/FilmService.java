package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    //    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate MIN_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);

    public Film add(Film film) {
        releaseDateCheck(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        releaseDateCheck(film);
        return filmStorage.update(film);
    }

    public Film findById(int id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void like(int filmId, int userId) {
        Film film = getById(filmId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().add(userId);
    }

    public void unlike(int filmId, int userId) {
        Film film = getById(filmId);
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().remove(userId);
    }

    public List<Film> topLikedFilms(Integer count) {
        List<Film> films = findAll();
        films.sort((f1, f2) -> f2.getLikes().size() - f1.getLikes().size());

        return films.subList(0, Math.min(count, films.size()));
    }

    private Film getById(int id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    private void releaseDateCheck(Film entity) { // перекинуть в Сервис!!
        if (entity.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
