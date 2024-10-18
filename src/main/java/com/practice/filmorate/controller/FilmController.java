package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films: {}", film);
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
        return filmService.findById(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        return filmService.update(film);
    }

    @PutMapping("/{film_id}/like/{id}")
    public void likeFilm(@PathVariable(required = false) Integer film_id,
                         @PathVariable(required = false) Integer id) {
        log.debug("Получен запрос PUT /films/{}/like/{}", film_id, id);

        if (film_id != null && film_id > 0
                && id != null & id > 0) {
            filmService.like(film_id, id);
        }
    }

    @DeleteMapping("/{film_id}/like/{id}")
    public void unlikeFilm(@PathVariable Integer film_id, @PathVariable Integer id) {
        log.debug("Получен запрос DELETE /films/{}/like/{}", film_id, id);
        filmService.unlike(film_id, id);
    }

    @GetMapping("/popular")
    public Collection<Film> topLikedFilms(
            @RequestParam(name = "count", required = false, defaultValue = "10") Integer count) {
        log.debug("Получен запрос GET /films/popular?count={}", count);
        return filmService.topLikedFilms(count);
    }
}
