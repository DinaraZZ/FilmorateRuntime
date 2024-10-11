package com.practice.filmorate.storage;

import com.practice.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    public Optional<Genre> findById(int id);

    public List<Genre> findAll();

    public List<Genre> findAllByFilmId(int filmId) ;

}
