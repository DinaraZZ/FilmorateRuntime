package com.practice.filmorate.service;

import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa add(Mpa mpa) {
        return mpaStorage.add(mpa);
    }

    public Mpa update(Mpa mpa) {
        return mpaStorage.update(mpa);
    }

    // todo
    public Optional<Mpa> findById(int id) {
        return mpaStorage.findById(id);
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
