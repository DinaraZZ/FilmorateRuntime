//package com.practice.filmorate.storage.impl;
//
//import com.practice.filmorate.exception.NotFoundException;
//import com.practice.filmorate.exception.ValidationException;
//import com.practice.filmorate.model.Film;
//import com.practice.filmorate.storage.FilmStorage;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//public class FilmStorageImpl implements FilmStorage {
//    private final Map<Integer, Film> films = new HashMap<>();
//    private int counter = 1;
//
//    @Override
//    public Film add(Film entity) {
//        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
//        if (entity.getReleaseDate().isBefore(minReleaseDate)) {
//            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
//        }
//
//        entity.setId(counter++);
//        films.put(entity.getId(), entity);
//        return entity;
//    }
//
//    @Override
//    public Film update(Film entity) {
//        if (!films.containsKey(entity.getId())) throw new NotFoundException("");
//
//        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
//        if (entity.getReleaseDate().isBefore(minReleaseDate)) {
//            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
//        }
//
//        films.put(entity.getId(), entity);
//        return entity;
//    }
//
//    @Override
//    public Optional<Film> findById(int id) { // ?
//        return Optional.ofNullable(films.get(id));
//    }
//
//    @Override
//    public List<Film> findAll() {
//        return films.values().stream().toList();
//    }
//}
