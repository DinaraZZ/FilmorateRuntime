package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Genre;
import com.practice.filmorate.storage.GenreStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL = "select * from genres";

    @Override
    public Optional<Genre> findById(int id) {
        return jdbcTemplate.queryForStream(
                        SELECT_ALL + " where id = ?", this::mapRow, id)
                .findFirst();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    @Override
    public List<Genre> findAllByFilmId(int filmId) {
        String genresSelect = """
                select genres.id, genres.name
                from genres
                         join films_genres on genres.id = films_genres.genre_id
                where films_genres.film_id = ?
                """;

        return jdbcTemplate.query(genresSelect, this::mapRow, filmId);
    }

    private Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
