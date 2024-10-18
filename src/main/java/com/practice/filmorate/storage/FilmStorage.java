package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

public interface FilmStorage extends Storage<Film>{
    //
    void addLike(int filmId, int userId);
    void removeLike(int filmId, int userId);
}
