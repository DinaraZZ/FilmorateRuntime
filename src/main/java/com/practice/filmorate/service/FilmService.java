package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    //    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    // todo: UserStorage
    private final UserService userService;
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

    // todo
    public Optional<Film> findById(int id) {
        return filmStorage.findById(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void like(int filmId, int userId) {
        Film film = getById(filmId);
        userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().add(userId);
    }

    // void
    public Film unlike(int filmId, int userId) {
        Film film = getById(filmId);
        userService.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        film.getLikes().remove(userId);

        return film;
    }

    // todo
    public List<Film> topLikedFilms(Integer count) {
        List<Film> films = findAll();
        films.sort((f1, f2) -> f2.getLikes().size() - f1.getLikes().size());
        return films.subList(0, Math.min(count, films.size()));
    }

    private Film getById(int id) {
        return filmStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Фильм не найден"));
    }

    private void releaseDateCheck(Film entity) { // перекинуть в Сервис!!
        if (entity.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
