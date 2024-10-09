package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final LocalDate MIN_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);
    private static final String SELECT_ALL = """
            select films.id, films.name, films.description,
                   films.release_date, films.duration, films.mpa_id, mpa.name mpa_name
            from FILMS join mpa on films.mpa_id = mpa.id
            """;

    @Override
    public Film add(Film entity) {
        if (entity.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "name", entity.getName(),
                "description", entity.getDescription(),
                "release_date", entity.getReleaseDate(),
                "duration", entity.getDuration(),
                "mpa_id", entity.getMpa().getId()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        entity.setId(id);
        return entity;
    }

    @Override
    public Film update(Film entity) {
        return null;
    }

    @Override
    public Optional<Film> findById(int id) {
        return jdbcTemplate.queryForStream(
                        SELECT_ALL + " where films.id = ?", this::mapRow, id)
                .findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Список жанров по id фильма
        String genresSelect = """
                select genres.id genre_id, genres.name genre_name
                from genres
                         join films_genres on genres.id = films_genres.genre_id
                where films_genres.film_id = ?
                """;

        List<Genre> genres = jdbcTemplate.query(genresSelect,
                (rs1, rowNum1) -> {
                    return Genre.builder()
                            .id(rs1.getInt("genre_id"))
                            .name(rs1.getString("genre_name"))
                            .build();
                }, rs.getInt("id"));

        Set<Genre> genreSet = new TreeSet<>(genres);

        // Список id пользователей, которые поставили лайк фильму
        String likesSelect = """
                select user_id
                from films_users_likes
                where film_id = ?
                """;
        List<Integer> likes = jdbcTemplate.query(likesSelect,
                (rs1, rowNum1) -> {
                    return rs1.getInt("user_id");
                }, rs.getInt("id"));
        System.out.println(likes);
        Set<Integer> likesSet = new TreeSet<>(likes);

        // Полностью собранный объект Film
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .genres(genreSet)
                .likes(likesSet)
                .build();
    }
}
