package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa findById(int id) {
        return mpaStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA не найден"));
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
