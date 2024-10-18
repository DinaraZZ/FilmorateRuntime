package com.practice.filmorate.controller;

import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.service.MpaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> findAll() {
        log.debug("Получен запрос GET /mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{id}") // ?
    public Mpa findById(@PathVariable int id) {
        log.debug("Получен запрос GET /mpa/{}", id);
        return mpaService.findById(id);
    }
}
