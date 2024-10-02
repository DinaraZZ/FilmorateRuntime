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
// тут логика
    // сторадж - хранение объектов
    // контроллер - вызывает из сервиса

    /*E update(E entity);
    Optional<E> findById(int id);
    List<E> findAll();*/
}
