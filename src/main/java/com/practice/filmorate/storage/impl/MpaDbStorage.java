package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL = "select * from mpa";

    @Override
    public Mpa add(Mpa entity) {
        return null;
    }

    @Override
    public Mpa update(Mpa entity) {
        return null;
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return jdbcTemplate.queryForStream(
                        SELECT_ALL + " where id = ?", this::mapRow, id)
                .findFirst();
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
