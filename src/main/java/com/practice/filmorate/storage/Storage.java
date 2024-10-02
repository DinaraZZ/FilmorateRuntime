package com.practice.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<E> {
    E add(E entity);
    E update(E entity);
    Optional<E> findById(int id);
    List<E> findAll();
}
