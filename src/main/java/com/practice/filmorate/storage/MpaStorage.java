package com.practice.filmorate.storage;

import com.practice.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<Mpa> findById(int id);
    List<Mpa> findAll();
}
