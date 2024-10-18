package com.practice.filmorate.controller;

import com.practice.filmorate.model.Genre;
import com.practice.filmorate.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private static final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> findAll() {
        log.debug("Получен запрос GET /genres");
        return genreService.findAll();
    }

    @GetMapping("/{id}") // ?
    public Genre findById(@PathVariable int id) {
        log.debug("Получен запрос GET /genres/{}", id);
        return genreService.findById(id);
    }
}
