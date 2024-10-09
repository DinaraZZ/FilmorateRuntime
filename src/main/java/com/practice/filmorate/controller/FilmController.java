package com.practice.filmorate.controller;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.service.FilmService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        return filmService.add(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET /films");
        return filmService.findAll();
    }

    @GetMapping("/{id}") // ?
    public Film findById(@PathVariable int id) {
        log.debug("Получен запрос GET /films/{}", id);
        return filmService.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        return filmService.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос PUT /films/{}/like/{}", id, userId);
        return filmService.like(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос DELETE /films/{}/like/{}", id, userId);
        return filmService.unlike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> topLikedFilms(
            @RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        log.debug("Получен запрос GET /films/popular?count={}", count);
        return filmService.topLikedFilms(count);
    }
}
