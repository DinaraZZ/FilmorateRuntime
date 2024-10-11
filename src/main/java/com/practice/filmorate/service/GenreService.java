package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.storage.GenreStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre findById(int id) {
        return genreStorage.findById(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }
}
